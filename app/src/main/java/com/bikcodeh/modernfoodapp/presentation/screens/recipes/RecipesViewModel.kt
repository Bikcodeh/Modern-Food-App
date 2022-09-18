package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.data.local.LocalDataSource
import com.bikcodeh.modernfoodapp.domain.common.Error
import com.bikcodeh.modernfoodapp.domain.common.fold
import com.bikcodeh.modernfoodapp.domain.common.toError
import com.bikcodeh.modernfoodapp.domain.common.validateHttpCodeErrorCode
import com.bikcodeh.modernfoodapp.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val localDataSource: LocalDataSource
) : ViewModel() {

    var canNavigateToFilter: Boolean = true
        private set

    private val _recipesState: MutableStateFlow<RecipesState> = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState>
        get() = _recipesState.asStateFlow()

    val favoriteRecipes =
        localDataSource.getAllFavorite().map { it.map { item -> item.toDomain() } }

    fun getLocalRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            localDataSource.getRecipes().collect { recipesData ->
                _recipesState.update { it.copy(recipes = recipesData) }
            }
        }
    }

    fun getRecipes(queries: Map<String, String>, initialFlow: Boolean) {
        _recipesState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.getRecipes(queries)
                .fold(
                    onSuccess = { recipes ->
                        if (initialFlow) localDataSource.insertRecipes(recipes.map { it.toEntity() })
                        _recipesState.update { it.copy(isLoading = false, error = null) }
                    },
                    onError = { errorCode, _ ->
                        val error = handleError(errorCode.validateHttpCodeErrorCode())
                        _recipesState.update {
                            it.copy(
                                isLoading = false,
                                error = error,
                                recipes = null
                            )
                        }
                    },
                    onException = { exception ->
                        val error = handleError(exception.toError())
                        _recipesState.update {
                            it.copy(
                                isLoading = false,
                                error = error,
                                recipes = null
                            )
                        }
                    }
                )
        }
    }

    fun searchRecipes(query: Map<String, String>) {
        _recipesState.update { it.copy(isLoading = true, error = null, recipes = null) }
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.searchRecipes(query)
                .fold(
                    onSuccess = { recipes ->
                        _recipesState.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                searchedRecipes = recipes
                            )
                        }
                    },
                    onError = { errorCode, _ ->
                        val error = handleError(errorCode.validateHttpCodeErrorCode())
                        _recipesState.update { it.copy(isLoading = false, error = error) }
                    },
                    onException = { exception ->
                        val error = handleError(exception.toError())
                        _recipesState.update { it.copy(isLoading = false, error = error) }
                    }
                )
        }
    }

    fun clearSearchedRecipes() {
        _recipesState.update { it.copy(searchedRecipes = null) }
        getLocalRecipes()
    }

    fun setNavigateToFilter(canNavigate: Boolean) {
        canNavigateToFilter = canNavigate
    }

    fun setAsFavorite(isFavorite: Boolean, recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            localDataSource.setFavorite(recipeId, isFavorite)
        }
    }

    private fun handleError(error: Error): RecipeError {
        return when (error) {
            Error.Connectivity -> RecipeError(
                errorMessage = R.string.connectivity_error,
                displayTryAgainBtn = true
            )
            is Error.HttpException -> RecipeError(
                errorMessage = error.messageResId,
                displayTryAgainBtn = true
            )
            is Error.Unknown -> RecipeError(
                errorMessage = R.string.connectivity_error,
                displayTryAgainBtn = false
            )
        }
    }
}
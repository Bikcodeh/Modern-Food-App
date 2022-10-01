package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.data.local.LocalDataSource
import com.bikcodeh.modernfoodapp.domain.common.Error
import com.bikcodeh.modernfoodapp.domain.common.fold
import com.bikcodeh.modernfoodapp.domain.common.toError
import com.bikcodeh.modernfoodapp.domain.common.validateHttpCodeErrorCode
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.domain.repository.FoodRepository
import com.bikcodeh.modernfoodapp.presentation.screens.detail.DetailState
import com.bikcodeh.modernfoodapp.presentation.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    private val localDataSource: LocalDataSource
) : BaseViewModel() {

    var canNavigateToFilter: Boolean = true
        private set
    var _isEditing: Boolean = false
        private set

    private val _recipesState: MutableStateFlow<RecipesState> = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState>
        get() = _recipesState.asStateFlow()

    private val _recipeDetail: MutableStateFlow<DetailState> = MutableStateFlow(DetailState.Idle)
    val recipeDetail: StateFlow<DetailState>
        get() = _recipeDetail.asStateFlow()

    val favoriteRecipes =
        localDataSource.getAllFavorite().map { it.map { item -> item.toDomain() } }

    fun setIsEditing(isEditing: Boolean) {
        _isEditing = isEditing
    }

    fun getLocalRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            localDataSource.getRecipes().collect { recipesData ->
                _recipesState.update { it.copy(recipes = recipesData, error = null) }
            }
        }
    }

    fun getRecipes(queries: Map<String, String>) {
        _recipesState.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.getRecipes(queries)
                .fold(
                    onSuccess = { recipes ->
                        localDataSource.insertRecipes(recipes.map { it.toEntity() })
                        _recipesState.update {
                            it.copy(
                                isLoading = false,
                                error = null,
                                recipes = recipes
                            )
                        }
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

    fun searchLocal(query: Map<String, String>) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipesSearched = localDataSource.searchRecipes(
                dietType = query["diet"] ?: "",
                type = query["type"] ?: ""
            )
            if (recipesSearched.isNotEmpty()) {
                _recipesState.update { it.copy(recipes = recipesSearched.map { recipeEntity -> recipeEntity.toDomain() }) }
            } else {
                getRecipes(query)
            }
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

    fun getRecipeById(recipeId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val recipe = localDataSource.getRecipeById(recipeId)
            recipe?.let {
                _recipeDetail.value = DetailState.Success(it.toDomain())
            } ?: run {
                _recipeDetail.value = DetailState.Success(null)
            }
        }
    }

    fun insertRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            localDataSource.insertRecipe(recipe.toEntity())
        }
    }
}
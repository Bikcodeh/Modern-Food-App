package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikcodeh.modernfoodapp.data.local.LocalDataSource
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.domain.common.fold
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

    private val _recipesState: MutableStateFlow<RecipesState> = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState>
        get() = _recipesState.asStateFlow()

    val recipes = localDataSource.getRecipes()

    fun getRecipes() {
        _recipesState.update { it.copy(isLoading = true, hasError = false) }
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.getRecipes().fold(
                onSuccess = { recipes ->
                    localDataSource.insertRecipes(recipes.map { it.toEntity() })
                    _recipesState.update { it.copy(isLoading = false, hasError = false) }
                },
                onError = { _, _ ->
                    _recipesState.update { it.copy(isLoading = false, hasError = true) }
                },
                onException = {
                    _recipesState.update { it.copy(isLoading = false, hasError = true) }
                }
            )
        }
    }

}
package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikcodeh.modernfoodapp.domain.common.fold
import com.bikcodeh.modernfoodapp.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val foodRepository: FoodRepository
) : ViewModel() {

    private val _recipesState: MutableStateFlow<RecipesState> = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState>
        get() = _recipesState.asStateFlow()

    fun getRecipes() {
        _recipesState.update { it.copy(isLoading = true) }
        viewModelScope.launch(Dispatchers.IO) {
            foodRepository.getRecipes().fold(
                onSuccess = { recipes ->
                    _recipesState.update { it.copy(data = recipes, isLoading = false) }
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
package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikcodeh.modernfoodapp.domain.repository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(
    private val foodRepository: FoodRepository
): ViewModel() {

    private val _recipesState: MutableStateFlow<RecipesState> = MutableStateFlow(RecipesState())
    val recipesState: StateFlow<RecipesState>
        get() = _recipesState.asStateFlow()

    fun getRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            //foodRepository.getRecipes()
        }
    }

}
package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import com.bikcodeh.modernfoodapp.domain.model.Recipe

data class RecipesState(
    val data: List<Recipe>? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false
)
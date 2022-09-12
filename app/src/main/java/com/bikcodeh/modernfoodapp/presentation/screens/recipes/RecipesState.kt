package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import androidx.annotation.StringRes

data class RecipesState(
    val isLoading: Boolean = false,
    val error: RecipeError? = null
)

data class RecipeError(
    val displayTryAgainBtn: Boolean = false,
    @StringRes val errorMessage: Int? = null,
)
package com.bikcodeh.modernfoodapp.presentation.screens.recipes

import androidx.annotation.StringRes
import com.bikcodeh.modernfoodapp.domain.model.Recipe

data class RecipesState(
    val isLoading: Boolean = false,
    val error: RecipeError? = null,
    val recipes: List<Recipe>? = null,
    val searchedRecipes: List<Recipe>? = null
)

data class RecipeError(
    val displayTryAgainBtn: Boolean = false,
    @StringRes val errorMessage: Int? = null,
)
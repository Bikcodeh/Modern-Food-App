package com.bikcodeh.modernfoodapp.presentation.screens.foodjoke

import com.bikcodeh.modernfoodapp.presentation.screens.recipes.RecipeError

data class FoodJokeState(
    val joke: String? = null,
    val isLoading: Boolean = false,
    val error: RecipeError? = null
)


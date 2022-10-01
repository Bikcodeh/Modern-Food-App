package com.bikcodeh.modernfoodapp.presentation.util

import androidx.lifecycle.ViewModel
import com.bikcodeh.modernfoodapp.R
import com.bikcodeh.modernfoodapp.domain.common.Error
import com.bikcodeh.modernfoodapp.presentation.screens.recipes.RecipeError

open class BaseViewModel : ViewModel() {

    fun handleError(error: Error): RecipeError {
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
            Error.LimitApi -> RecipeError(
                errorMessage = R.string.limited_points_error,
                isLimitError = true
            )
        }
    }
}
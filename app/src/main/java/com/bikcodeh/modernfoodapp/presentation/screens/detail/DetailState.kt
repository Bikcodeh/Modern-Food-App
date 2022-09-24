package com.bikcodeh.modernfoodapp.presentation.screens.detail

import com.bikcodeh.modernfoodapp.domain.model.Recipe

sealed class DetailState {
    object Idle: DetailState()
    data class Success(val recipe: Recipe?): DetailState()
}
package com.bikcodeh.modernfoodapp.domain.repository

import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.model.Recipe

interface FoodRepository {

    suspend fun getRecipes(): Result<List<Recipe>>
}
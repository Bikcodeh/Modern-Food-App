package com.bikcodeh.modernfoodapp.domain.repository

import androidx.paging.PagingData
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

    suspend fun getRecipes(queries: Map<String, String>): Flow<PagingData<RecipeEntity>>
    suspend fun searchRecipes(queries: Map<String, String>): Result<List<Recipe>>
}
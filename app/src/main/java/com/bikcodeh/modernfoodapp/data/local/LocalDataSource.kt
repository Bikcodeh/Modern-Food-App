package com.bikcodeh.modernfoodapp.data.local

import com.bikcodeh.modernfoodapp.data.local.dao.RecipesDao
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {
    fun getRecipes(): Flow<List<RecipeEntity>> {
        return recipesDao.getRecipes()
    }

    suspend fun insertRecipes(recipeEntity: RecipeEntity) {
        recipesDao.insertRecipes(recipeEntity)
    }
}
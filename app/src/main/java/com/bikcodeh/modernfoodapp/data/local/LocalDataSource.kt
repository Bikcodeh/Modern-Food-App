package com.bikcodeh.modernfoodapp.data.local

import com.bikcodeh.modernfoodapp.data.local.dao.RecipesDao
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {
    fun getRecipes(): Flow<List<Recipe>> {
        return recipesDao.getRecipes().map { it.map { recipeEntity -> recipeEntity.toDomain() } }
    }

    suspend fun insertRecipes(recipeEntity: List<RecipeEntity>) {
        recipesDao.insertRecipes(recipeEntity)
    }
}
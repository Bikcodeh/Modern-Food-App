package com.bikcodeh.modernfoodapp.data.local

import com.bikcodeh.modernfoodapp.data.local.dao.RecipesDao
import com.bikcodeh.modernfoodapp.data.local.entity.FavoriteRecipeEntity
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

    suspend fun clear() {
        recipesDao.clear()
    }

    suspend fun insertFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) {
        recipesDao.insertFavoriteRecipe(favoriteRecipeEntity)
    }

    fun getAllFavorite(): Flow<List<FavoriteRecipeEntity>> {
        return recipesDao.getAllFavorites()
    }

    suspend fun deleteFavorite(favoriteRecipeEntity: FavoriteRecipeEntity) {
        recipesDao.deleteFavorite(favoriteRecipeEntity)
    }

    suspend fun clearFavorites() {
        recipesDao.clearFavorites()
    }
}
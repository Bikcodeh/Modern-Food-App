package com.bikcodeh.modernfoodapp.data.local

import com.bikcodeh.modernfoodapp.data.local.dao.FoodJokeDao
import com.bikcodeh.modernfoodapp.data.local.dao.RecipesDao
import com.bikcodeh.modernfoodapp.data.local.entity.FoodJokeEntity
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao,
    private val foodJokeDao: FoodJokeDao
) {
    fun getRecipes(): Flow<List<Recipe>> {
        return recipesDao.getRecipes().map { it.map { recipeEntity -> recipeEntity.toDomain() } }
    }

    suspend fun insertRecipes(recipeEntity: List<RecipeEntity>) {
        recipesDao.insertRecipes(recipeEntity)
    }

    fun getAllFavorite(): Flow<List<RecipeEntity>> {
        return recipesDao.getAllFavorites()
    }

    suspend fun setFavorite(recipeId: Int, isFavorite: Boolean) {
        recipesDao.setFavorite(recipeId, isFavorite)
    }

    suspend fun getRecipeById(recipeId: Int): RecipeEntity? {
        return recipesDao.getRecipeById(recipeId)
    }

    suspend fun insertRecipe(recipeEntity: RecipeEntity) {
        recipesDao.insertRecipe(recipeEntity)
    }

    suspend fun searchRecipes(dietType: String, type: String): List<RecipeEntity> {
        return recipesDao.searchRecipes("%$dietType%", "%$type%")
    }

    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity) {
        foodJokeDao.insertFoodJoke(foodJokeEntity)
    }

    suspend fun getAllFoodJokes(): List<FoodJokeEntity> {
        return foodJokeDao.getAllFoodJokes()
    }
}
package com.bikcodeh.modernfoodapp.data.local.dao

import androidx.room.*
import com.bikcodeh.modernfoodapp.data.local.entity.FavoriteRecipeEntity
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Query("DELETE FROM recipes")
    suspend fun clear()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoriteRecipeEntity: FavoriteRecipeEntity)

    @Query("SELECT * FROM favorite_recipe ORDER BY id ASC")
    fun getAllFavorites(): Flow<List<FavoriteRecipeEntity>>

    @Delete
    suspend fun deleteFavorite(favoriteRecipeEntity: FavoriteRecipeEntity)

    @Query("DELETE FROM favorite_recipe")
    suspend fun clearFavorites()
}
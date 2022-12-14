package com.bikcodeh.modernfoodapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)

    @Query("SELECT * FROM recipes ORDER BY id ASC")
    fun getRecipes(): Flow<List<RecipeEntity>>

    @Query("SELECT * FROM recipes WHERE isFavorite = 1")
    fun getAllFavorites(): Flow<List<RecipeEntity>>

    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun setFavorite(recipeId: Int, isFavorite: Boolean)

    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipeEntity: RecipeEntity)

    @Query("SELECT * FROM recipes WHERE diet LIKE :dietType AND type LIKE :type")
    suspend fun searchRecipes(dietType: String, type: String): List<RecipeEntity>
}
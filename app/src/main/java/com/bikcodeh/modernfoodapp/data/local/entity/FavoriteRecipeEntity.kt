package com.bikcodeh.modernfoodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_recipe")
data class FavoriteRecipeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val recipeEntity: RecipeEntity
)
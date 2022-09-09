package com.bikcodeh.modernfoodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bikcodeh.modernfoodapp.domain.model.Recipe

@Entity(tableName = "recipes")
class RecipeEntity(
    var foodRecipe: Recipe
) {
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
}

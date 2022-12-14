package com.bikcodeh.modernfoodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bikcodeh.modernfoodapp.domain.model.ExtendedIngredient
import com.bikcodeh.modernfoodapp.domain.model.Recipe

@Entity(tableName = "recipes")
class RecipeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val aggregateLikes: Int,
    val cheap: Boolean,
    val dairyFree: Boolean,
    val extendedIngredients: List<ExtendedIngredient>,
    val glutenFree: Boolean,
    val image: String,
    val readyInMinutes: Int,
    val sourceName: String,
    val sourceUrl: String,
    val summary: String,
    val title: String,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean,
    val isFavorite: Boolean = false,
    val diet: List<String>,
    val type: List<String>
) {
    fun toDomain(): Recipe = Recipe(
        aggregateLikes,
        cheap,
        dairyFree,
        extendedIngredients,
        glutenFree,
        id,
        image,
        readyInMinutes,
        sourceName,
        sourceUrl,
        summary,
        title,
        vegan,
        vegetarian,
        veryHealthy,
        isFavorite,
        diet,
        type
    )
}

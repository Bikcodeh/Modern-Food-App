package com.bikcodeh.modernfoodapp.domain.model

import android.os.Parcelable
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val aggregateLikes: Int,
    val cheap: Boolean,
    val dairyFree: Boolean,
    val extendedIngredients: List<ExtendedIngredient>,
    val glutenFree: Boolean,
    val id: Int,
    val image: String,
    val readyInMinutes: Int,
    val sourceName: String,
    val sourceUrl: String,
    val summary: String,
    val title: String,
    val vegan: Boolean,
    val vegetarian: Boolean,
    val veryHealthy: Boolean
): Parcelable {
    fun toEntity(): RecipeEntity = RecipeEntity(
        id,
        aggregateLikes,
        cheap,
        dairyFree,
        extendedIngredients,
        glutenFree,
        image,
        readyInMinutes,
        sourceName,
        sourceUrl,
        summary,
        title,
        vegan,
        vegetarian,
        veryHealthy
    )
}

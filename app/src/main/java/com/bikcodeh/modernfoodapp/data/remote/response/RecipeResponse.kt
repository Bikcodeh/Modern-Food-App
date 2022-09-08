package com.bikcodeh.modernfoodapp.data.remote.response

import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.google.gson.annotations.SerializedName

data class RecipeResponse(
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int,
    @SerializedName("cheap")
    val cheap: Boolean,
    @SerializedName("dairyFree")
    val dairyFree: Boolean,
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredientResponse>,
    @SerializedName("glutenFree")
    val glutenFree: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: String,
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    @SerializedName("sourceName")
    val sourceName: String,
    @SerializedName("sourceUrl")
    val sourceUrl: String,
    @SerializedName("summary")
    val summary: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("vegan")
    val vegan: Boolean,
    @SerializedName("vegetarian")
    val vegetarian: Boolean,
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean
) {
    fun toDomain(): Recipe = Recipe(
        aggregateLikes,
        cheap,
        dairyFree,
        extendedIngredients.map { it.toDomain() },
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
        veryHealthy
    )
}
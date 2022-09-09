package com.bikcodeh.modernfoodapp.data.remote.response

import com.bikcodeh.modernfoodapp.domain.model.ExtendedIngredient
import com.google.gson.annotations.SerializedName

data class ExtendedIngredientResponse(
    @SerializedName("amount")
    val amount: Double,
    @SerializedName("consistency")
    val consistency: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("original")
    val original: String,
    @SerializedName("unit")
    val unit: String
) {
    fun toDomain(): ExtendedIngredient = ExtendedIngredient(
        amount, consistency, image ?: "", name, original, unit
    )
}
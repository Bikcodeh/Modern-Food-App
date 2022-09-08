package com.bikcodeh.modernfoodapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class FoodResponse(
    @SerializedName("results")
    val results: List<RecipeResponse>
)
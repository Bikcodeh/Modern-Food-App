package com.bikcodeh.modernfoodapp.data.remote.api

import com.bikcodeh.modernfoodapp.BuildConfig
import com.bikcodeh.modernfoodapp.data.remote.response.FoodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>
    ): Response<FoodResponse>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap query: Map<String, String>
    ): Response<FoodResponse>

    @GET("recipes/{recipeId}/information")
    suspend fun searchRecipeById(
        @Query("apiKey") key: String = BuildConfig.FOOD_KEY,
        @Path("recipeId") recipeId: Int
    ): Response<FoodResponse>
}
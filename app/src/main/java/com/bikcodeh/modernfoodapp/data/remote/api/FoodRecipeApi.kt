package com.bikcodeh.modernfoodapp.data.remote.api

import com.bikcodeh.modernfoodapp.BuildConfig
import com.bikcodeh.modernfoodapp.data.remote.response.FoodJokeResponse
import com.bikcodeh.modernfoodapp.data.remote.response.FoodResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface FoodRecipeApi {

    @GET("recipes/complexSearch")
    suspend fun getRecipes(
        @QueryMap queries: Map<String, String>,
        @Query("offset") offset: Int = 0
    ): Response<FoodResponse>

    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @QueryMap query: Map<String, String>,
        @Query("offset") offset: Int = 0
    ): Response<FoodResponse>

    @GET("food/jokes/random")
    suspend fun getRandomFoodJoke(
        @Query("apiKey") apiKey: String = BuildConfig.FOOD_KEY
    ): Response<FoodJokeResponse>
}
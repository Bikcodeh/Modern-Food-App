package com.bikcodeh.modernfoodapp.data.remote.api

import com.bikcodeh.modernfoodapp.data.remote.response.FoodResponse
import retrofit2.Response
import retrofit2.http.GET
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
}
package com.bikcodeh.modernfoodapp.data.remote

import com.bikcodeh.modernfoodapp.BuildConfig
import com.bikcodeh.modernfoodapp.data.remote.api.FoodRecipeApi
import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.common.fold
import com.bikcodeh.modernfoodapp.domain.common.makeSafeRequest
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {

    suspend fun getRecipes(): Result<List<Recipe>> {
        val queries = HashMap<String, String>()
        queries["number"] = "50"
        queries["apiKey"] = BuildConfig.FOOD_KEY
        queries["type"] = "snack"
        queries["diet"] = "vegan"
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"

        val result = makeSafeRequest { foodRecipeApi.getRecipes(queries) }

        return result.fold(
            onSuccess = {
                Result.Success(it.results.map { recipeResponse -> recipeResponse.toDomain() })
            },
            onError = { code, message ->
                Result.Error(code, message)
            },
            onException = { exception ->
                Result.Exception(exception)
            }
        )
    }
}
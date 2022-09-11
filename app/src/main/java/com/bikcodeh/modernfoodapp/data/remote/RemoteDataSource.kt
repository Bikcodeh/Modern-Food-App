package com.bikcodeh.modernfoodapp.data.remote

import com.bikcodeh.modernfoodapp.BuildConfig
import com.bikcodeh.modernfoodapp.data.remote.api.FoodRecipeApi
import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.common.fold
import com.bikcodeh.modernfoodapp.domain.common.makeSafeRequest
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_RECIPES_TOTAL
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {

    suspend fun getRecipes(mealType: String, dietType: String): Result<List<Recipe>> {
        val queries = HashMap<String, String>()
        queries["number"] = DEFAULT_RECIPES_TOTAL
        queries["apiKey"] = BuildConfig.FOOD_KEY
        queries["type"] = mealType
        queries["diet"] = dietType
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
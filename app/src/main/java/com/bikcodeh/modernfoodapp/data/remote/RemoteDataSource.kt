package com.bikcodeh.modernfoodapp.data.remote

import com.bikcodeh.modernfoodapp.data.remote.api.FoodRecipeApi
import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.common.fold
import com.bikcodeh.modernfoodapp.domain.common.makeSafeRequest
import com.bikcodeh.modernfoodapp.domain.model.FoodJoke
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Result<List<Recipe>> {
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

    suspend fun searchRecipes(query: Map<String, String>): Result<List<Recipe>> {
        val result = makeSafeRequest { foodRecipeApi.searchRecipes(query) }

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

    suspend fun getFoodJoke(): Result<FoodJoke> {
        val result = makeSafeRequest { foodRecipeApi.getRandomFoodJoke() }
        return result.fold(
            onSuccess = {
                Result.Success(it.toDomain())
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
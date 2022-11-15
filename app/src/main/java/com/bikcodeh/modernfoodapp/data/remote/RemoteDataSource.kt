package com.bikcodeh.modernfoodapp.data.remote

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bikcodeh.modernfoodapp.data.local.RecipesDatabase
import com.bikcodeh.modernfoodapp.data.local.dao.RecipesDao
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.data.remote.api.FoodRecipeApi
import com.bikcodeh.modernfoodapp.data.repository.RecipesRemoteMediator
import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.common.fold
import com.bikcodeh.modernfoodapp.domain.common.makeSafeRequest
import com.bikcodeh.modernfoodapp.domain.model.FoodJoke
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalPagingApi
class RemoteDataSource @Inject constructor(
    private val foodRecipeApi: FoodRecipeApi,
    private val recipesDao: RecipesDao,
    private val recipesDatabase: RecipesDatabase
) {
    private val recipeSourceFactory = { recipesDao.getRecipes() }

    fun getRecipes(queries: Map<String, String>): Flow<PagingData<RecipeEntity>> {

        return Pager(
            PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            null,
            RecipesRemoteMediator(
                recipesDatabase = recipesDatabase,
                foodRecipeApi = foodRecipeApi,
                queries = queries
            ),
            recipeSourceFactory
        ).flow
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

    companion object {
        const val NETWORK_PAGE_SIZE = 10
    }
}
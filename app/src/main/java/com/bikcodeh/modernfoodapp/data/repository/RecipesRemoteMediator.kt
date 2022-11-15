package com.bikcodeh.modernfoodapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.bikcodeh.modernfoodapp.data.local.RecipesDatabase
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.data.local.entity.RemoteKeysEntity
import com.bikcodeh.modernfoodapp.data.remote.api.FoodRecipeApi
import com.bikcodeh.modernfoodapp.domain.common.getException
import com.bikcodeh.modernfoodapp.domain.common.getHttpErrorCode
import com.bikcodeh.modernfoodapp.domain.common.getSuccess
import com.bikcodeh.modernfoodapp.domain.common.makeSafeRequest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalPagingApi
class RecipesRemoteMediator(
    private val recipesDatabase: RecipesDatabase,
    private val foodRecipeApi: FoodRecipeApi,
    private val queries: Map<String, String>
) : RemoteMediator<Int, RecipeEntity>() {

    private val remoteKeysDao = recipesDatabase.remoteKeysDao()
    private val recipesDao = recipesDatabase.recipesDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, RecipeEntity>
    ): MediatorResult {
        val offset = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: RECIPES_STARTING_OFFSET
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeysForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                prevKey
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeysForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey
            }
        }

        try {
            val response = makeSafeRequest { foodRecipeApi.getRecipes(queries) }

            response.getException()?.let {
                throw it
            }
            response.getHttpErrorCode()?.let {
                throw HttpException(
                    Response.error<ResponseBody>(
                        it,
                        "".toResponseBody("plain/text".toMediaType())
                    )
                )
            }
            val recipes = response.getSuccess()?.results?.map { it.toDomain() }.orEmpty()
            val endOfPagination = recipes.isEmpty()

            recipesDatabase.withTransaction {
                // Initial load of data
                if (loadType == LoadType.REFRESH) {
                    remoteKeysDao.clearRemoteKeys()
                    recipesDao.clear()
                }

                val prevKey = if (offset > RECIPES_STARTING_OFFSET) offset.minus(1) else null
                val nextKey = if (endOfPagination) null else offset.plus(1)
                val keys = recipes.map {
                    RemoteKeysEntity(
                        recipeId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                remoteKeysDao.insertAll(keys)
                recipesDao.insertRecipes(recipes.map { it.toEntity() })
            }
            return MediatorResult.Success(endOfPaginationReached = endOfPagination)
        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }
    }

    private suspend fun getRemoteKeysForLastItem(state: PagingState<Int, RecipeEntity>): RemoteKeysEntity? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { recipe ->
                remoteKeysDao.remoteKeysRecipeId(recipe.id)
            }
    }

    private suspend fun getRemoteKeysForFirstItem(state: PagingState<Int, RecipeEntity>): RemoteKeysEntity? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { recipe ->
                // Get the remote keys of the first items retrieved
                remoteKeysDao.remoteKeysRecipeId(recipe.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, RecipeEntity>
    ): RemoteKeysEntity? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { recipeId ->
                remoteKeysDao.remoteKeysRecipeId(recipeId)
            }
        }
    }

    companion object {
        private const val RECIPES_STARTING_OFFSET = 0
    }
}
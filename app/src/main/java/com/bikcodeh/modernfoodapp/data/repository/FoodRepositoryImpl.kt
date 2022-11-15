package com.bikcodeh.modernfoodapp.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.data.remote.RemoteDataSource
import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.domain.repository.FoodRepository
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_DIET_TYPE
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_MEAL_TYPE
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRepositoryImpl @OptIn(ExperimentalPagingApi::class)
@Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : FoodRepository {

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun getRecipes(queries: Map<String, String>): Flow<PagingData<RecipeEntity>> {
        return remoteDataSource.getRecipes(queries)
    }

    @OptIn(ExperimentalPagingApi::class)
    override suspend fun searchRecipes(queries: Map<String, String>): Result<List<Recipe>> {
        return remoteDataSource.searchRecipes(queries)
    }
}
package com.bikcodeh.modernfoodapp.data.repository

import com.bikcodeh.modernfoodapp.data.remote.RemoteDataSource
import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.domain.repository.FoodRepository
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_DIET_TYPE
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_MEAL_TYPE
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : FoodRepository {

    override suspend fun getRecipes(queries: Map<String, String>): Result<List<Recipe>> {
        return remoteDataSource.getRecipes(queries)
    }

    override suspend fun searchRecipes(queries: Map<String, String>): Result<List<Recipe>> {
        return remoteDataSource.searchRecipes(queries)
    }
}
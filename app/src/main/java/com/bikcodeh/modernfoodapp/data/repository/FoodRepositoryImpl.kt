package com.bikcodeh.modernfoodapp.data.repository

import com.bikcodeh.modernfoodapp.data.remote.RemoteDataSource
import com.bikcodeh.modernfoodapp.domain.common.Result
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.bikcodeh.modernfoodapp.domain.repository.FoodRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : FoodRepository {

    override suspend fun getRecipes(): Result<List<Recipe>> {
        return remoteDataSource.getRecipes()
    }
}
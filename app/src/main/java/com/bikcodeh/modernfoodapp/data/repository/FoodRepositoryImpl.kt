package com.bikcodeh.modernfoodapp.data.repository

import com.bikcodeh.modernfoodapp.data.remote.RemoteDataSource
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class FoodRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) {

}
package com.bikcodeh.modernfoodapp.di

import com.bikcodeh.modernfoodapp.data.remote.RemoteDataSource
import com.bikcodeh.modernfoodapp.data.repository.FoodRepositoryImpl
import com.bikcodeh.modernfoodapp.domain.repository.FoodRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    @ViewModelScoped
    fun providesFoodRepository(remoteDataSource: RemoteDataSource): FoodRepository =
        FoodRepositoryImpl(remoteDataSource)
}
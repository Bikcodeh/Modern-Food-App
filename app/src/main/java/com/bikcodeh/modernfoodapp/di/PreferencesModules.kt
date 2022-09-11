package com.bikcodeh.modernfoodapp.di

import android.content.Context
import com.bikcodeh.modernfoodapp.data.local.preferences.FoodDataStoreOperations
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object PreferencesModules {

    @Provides
    @ViewModelScoped
    fun providesDataStoreOperations(@ApplicationContext context: Context): FoodDataStoreOperations =
        FoodDataStoreOperations(context)
}
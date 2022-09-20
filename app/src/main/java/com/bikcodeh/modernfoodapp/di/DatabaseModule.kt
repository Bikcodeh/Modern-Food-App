package com.bikcodeh.modernfoodapp.di

import android.content.Context
import androidx.room.Room
import com.bikcodeh.modernfoodapp.data.local.RecipesDatabase
import com.bikcodeh.modernfoodapp.data.local.dao.RecipesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context): RecipesDatabase =
        Room.databaseBuilder(
            context,
            RecipesDatabase::class.java,
            RecipesDatabase.DB_NAME
        ).createFromAsset("database/recipes.db").build()

    @Singleton
    @Provides
    fun providesRecipeDao(recipesDatabase: RecipesDatabase): RecipesDao =
        recipesDatabase.recipesDao()
}
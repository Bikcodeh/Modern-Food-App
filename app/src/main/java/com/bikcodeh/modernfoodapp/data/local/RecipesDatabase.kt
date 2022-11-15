package com.bikcodeh.modernfoodapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bikcodeh.modernfoodapp.data.local.converter.RecipesTypeConverter
import com.bikcodeh.modernfoodapp.data.local.dao.FoodJokeDao
import com.bikcodeh.modernfoodapp.data.local.dao.RecipesDao
import com.bikcodeh.modernfoodapp.data.local.dao.RemoteKeysDao
import com.bikcodeh.modernfoodapp.data.local.entity.FoodJokeEntity
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity
import com.bikcodeh.modernfoodapp.data.local.entity.RemoteKeysEntity

@Database(
    entities = [RecipeEntity::class, FoodJokeEntity::class, RemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao
    abstract fun foodJokeDao(): FoodJokeDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DB_NAME = "recipes.db"
    }
}
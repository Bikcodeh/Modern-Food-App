package com.bikcodeh.modernfoodapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bikcodeh.modernfoodapp.data.local.converter.RecipesTypeConverter
import com.bikcodeh.modernfoodapp.data.local.dao.FoodJokeDao
import com.bikcodeh.modernfoodapp.data.local.dao.RecipesDao
import com.bikcodeh.modernfoodapp.data.local.entity.FoodJokeEntity
import com.bikcodeh.modernfoodapp.data.local.entity.RecipeEntity

@Database(
    entities = [RecipeEntity::class, FoodJokeEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase : RoomDatabase() {

    abstract fun recipesDao(): RecipesDao
    abstract fun foodJokeDao(): FoodJokeDao

    companion object {
        const val DB_NAME = "recipes.db"
    }
}
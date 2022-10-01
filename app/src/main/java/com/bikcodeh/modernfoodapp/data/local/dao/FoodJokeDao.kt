package com.bikcodeh.modernfoodapp.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bikcodeh.modernfoodapp.data.local.entity.FoodJokeEntity

@Dao
interface FoodJokeDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFoodJoke(foodJokeEntity: FoodJokeEntity)

    @Query("SELECT * FROM food_joke")
    suspend fun getAllFoodJokes(): List<FoodJokeEntity>
}
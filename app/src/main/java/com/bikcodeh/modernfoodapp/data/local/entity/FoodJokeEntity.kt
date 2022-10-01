package com.bikcodeh.modernfoodapp.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bikcodeh.modernfoodapp.domain.model.FoodJoke

@Entity(tableName = "food_joke")
data class FoodJokeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val description: String
) {
    fun toDomain(): FoodJoke = FoodJoke(description)
}

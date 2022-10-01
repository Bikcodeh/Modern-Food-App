package com.bikcodeh.modernfoodapp.domain.model

import com.bikcodeh.modernfoodapp.data.local.entity.FoodJokeEntity

data class FoodJoke(
    val text: String
) {
    fun toEntity(): FoodJokeEntity = FoodJokeEntity(description = text)
}

package com.bikcodeh.modernfoodapp.data.remote.response

import com.bikcodeh.modernfoodapp.domain.model.FoodJoke

data class FoodJokeResponse(
    val text: String
) {
    fun toDomain(): FoodJoke = FoodJoke(text)
}

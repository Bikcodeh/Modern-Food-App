package com.bikcodeh.modernfoodapp.data.local.converter

import androidx.room.TypeConverter
import com.bikcodeh.modernfoodapp.domain.model.Recipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    @TypeConverter
    fun foodRecipeToString(recipe: Recipe): String {
        return Gson().toJson(recipe)
    }

    @TypeConverter
    fun stringToRecipe(data: String): Recipe {
        val listType = object : TypeToken<Recipe>() {}.type
        return Gson().fromJson(data, listType)
    }
}
package com.bikcodeh.modernfoodapp.data.local.converter

import androidx.room.TypeConverter
import com.bikcodeh.modernfoodapp.domain.model.ExtendedIngredient
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

    @TypeConverter
    fun fromExtendedIngredientList(value: List<ExtendedIngredient>): String {
        val gson = Gson()
        val type = object : TypeToken<List<ExtendedIngredient>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toExtendedIngredientList(value: String): List<ExtendedIngredient> {
        val gson = Gson()
        val type = object : TypeToken<List<ExtendedIngredient>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromString(value: String?): List<String?>? {
        val listType = object : TypeToken<List<String?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}
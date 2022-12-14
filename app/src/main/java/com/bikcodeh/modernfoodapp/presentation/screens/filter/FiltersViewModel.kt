package com.bikcodeh.modernfoodapp.presentation.screens.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bikcodeh.modernfoodapp.BuildConfig
import com.bikcodeh.modernfoodapp.data.local.preferences.FoodDataStoreOperations
import com.bikcodeh.modernfoodapp.util.Constants
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_DIET_TYPE
import com.bikcodeh.modernfoodapp.util.Constants.DEFAULT_MEAL_TYPE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val foodDataStoreOperations: FoodDataStoreOperations
): ViewModel() {

    private var mealType = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    private val _fetchNewData = Channel<FilterState>()
    val fetchNewData: Flow<FilterState>
        get() = _fetchNewData.receiveAsFlow()


    val filters = foodDataStoreOperations.readMealAndDietType

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            foodDataStoreOperations.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }


    fun setFetchNewData() {
        viewModelScope.launch(Dispatchers.IO) {
            _fetchNewData.send(FilterState.FetchData)
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            foodDataStoreOperations.readMealAndDietType.collect { value ->
                mealType = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries["number"] = Constants.DEFAULT_RECIPES_TOTAL
        queries["apiKey"] = BuildConfig.FOOD_KEY
        queries["type"] = mealType
        queries["diet"] = dietType
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"

        return queries
    }

    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()
        queries["query"] = searchQuery
        queries["number"] = Constants.DEFAULT_RECIPES_TOTAL
        queries["apiKey"] = BuildConfig.FOOD_KEY
        queries["addRecipeInformation"] = "true"
        queries["fillIngredients"] = "true"
        return queries
    }
}
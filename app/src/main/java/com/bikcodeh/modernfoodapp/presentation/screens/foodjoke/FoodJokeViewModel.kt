package com.bikcodeh.modernfoodapp.presentation.screens.foodjoke

import androidx.lifecycle.viewModelScope
import com.bikcodeh.modernfoodapp.data.local.LocalDataSource
import com.bikcodeh.modernfoodapp.data.remote.RemoteDataSource
import com.bikcodeh.modernfoodapp.domain.common.fold
import com.bikcodeh.modernfoodapp.domain.common.toError
import com.bikcodeh.modernfoodapp.domain.common.validateHttpCodeErrorCode
import com.bikcodeh.modernfoodapp.presentation.util.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FoodJokeViewModel @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : BaseViewModel() {

    private val _foodJokeState: MutableStateFlow<FoodJokeState> = MutableStateFlow(FoodJokeState())
    val foodJokeState: StateFlow<FoodJokeState>
        get() = _foodJokeState.asStateFlow()

    fun getLocalFoodJokes() {
        viewModelScope.launch(Dispatchers.IO) {
            val jokes = withContext(Dispatchers.IO) {
                localDataSource.getAllFoodJokes().map { it.toDomain() }
            }
            if (jokes.isNotEmpty()) {
                _foodJokeState.update {
                    it.copy(
                        joke = jokes.random().text,
                        isLoading = false
                    )
                }
            } else {
                _foodJokeState.update {
                    it.copy(
                        joke = "",
                        isLoading = false
                    )
                }
            }
        }
    }

    fun getRemoteFoodJoke() {
        _foodJokeState.update { it.copy(isLoading = true, error = null, joke = null) }
        viewModelScope.launch(Dispatchers.IO) {
            remoteDataSource.getFoodJoke().fold(
                onSuccess = { joke ->
                    localDataSource.insertFoodJoke(joke.toEntity())
                    _foodJokeState.update {
                        it.copy(
                            joke = joke.text,
                            error = null,
                            isLoading = false
                        )
                    }
                },
                onException = { exception ->
                    val error = handleError(exception.toError())
                    _foodJokeState.update {
                        it.copy(
                            joke = null,
                            error = error,
                            isLoading = false
                        )
                    }
                },
                onError = { errorCode, _ ->
                    val error = handleError(errorCode.validateHttpCodeErrorCode())
                    _foodJokeState.update {
                        it.copy(
                            joke = null,
                            error = error,
                            isLoading = false
                        )
                    }
                }
            )
        }
    }
}
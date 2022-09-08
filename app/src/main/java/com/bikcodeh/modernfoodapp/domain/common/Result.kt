package com.bikcodeh.modernfoodapp.domain.common


sealed class Result<T> {
    class Success<T>(val data: T) : Result<T>()
    class Error<T : Any>(val code: Int, val message: String?) : Result<T>()
    class Exception<T : Any>(val exception: kotlin.Exception) : Result<T>()
}

inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onError: (code: Int, message: String?) -> R,
    onException: (exception: Exception) -> R
): R = when (this) {
    is Result.Success -> onSuccess(data)
    is Result.Error -> onError(code, message)
    is Result.Exception -> onException(exception)
}
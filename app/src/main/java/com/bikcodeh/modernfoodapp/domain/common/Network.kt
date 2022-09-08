package com.bikcodeh.modernfoodapp.domain.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response
import timber.log.Timber

suspend fun <T : Any> makeSafeRequest(
    execute: suspend () -> Response<T>
): Result<T> {
    return withContext(Dispatchers.IO) {
        try {
            val response = execute()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                Result.Error(code = response.code(), message = response.message())
            }
        } catch (e: Exception) {
            Timber.e(e)
            Result.Exception(e)
        }
    }
}
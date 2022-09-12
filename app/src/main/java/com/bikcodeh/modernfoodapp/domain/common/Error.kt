package com.bikcodeh.modernfoodapp.domain.common

import androidx.annotation.StringRes
import com.bikcodeh.modernfoodapp.R
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

sealed class Error {
    object Connectivity : Error()
    data class Unknown(val message: String) : Error()
    class HttpException(@StringRes val messageResId: Int): Error()
}

fun Exception.toError(): Error = when (this) {
    is IOException,
    is UnknownHostException -> Error.Connectivity
    is HttpException -> HttpErrors.handleError(code())
    else -> Error.Unknown(message ?: "")
}

fun Int.validateHttpCodeErrorCode(): Error {
    return HttpErrors.handleError(this)
}

object HttpErrors {
    fun handleError(errorCode: Int): Error {
        val errorResId = when (errorCode) {
            503 -> R.string.service_unavailable_error
            500 -> R.string.internal_server_error
            404 -> R.string.not_found_error
            400 -> R.string.invalid_request_error
            401 -> R.string.unauthorized_error
            402 -> R.string.limited_points_error
            else -> R.string.unknown_error
        }
        return Error.HttpException(messageResId = errorResId)
    }
}
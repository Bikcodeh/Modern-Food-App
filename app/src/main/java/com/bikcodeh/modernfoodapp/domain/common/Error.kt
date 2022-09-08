package com.bikcodeh.modernfoodapp.domain.common

import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

sealed class Error {
    class Server(val code: Int) : Error()
    object Connectivity : Error()
    data class Unknown(val message: String) : Error()
}

fun Exception.toError(): Error = when (this) {
    is IOException,
    is UnknownHostException -> Error.Connectivity
    is HttpException -> Error.Server(code())
    else -> Error.Unknown(message ?: "")
}
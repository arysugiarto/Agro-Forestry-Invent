package com.agro.inventory.data.remote

sealed class Result <T> {
    class Nothing<T> : Result<T>() {
        override fun toString() = "Result.Nothing"
    }
    class Loading<T> : Result<T>()
    class Success<T>(val data: T?) : Result<T>()
    class Error<T>(
        val message: String,
    ): Result<T>()

    companion object {
        fun <T> nothing() = Nothing<T>()
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T?) = Success(data)
        fun <T> error(
            message: String,
            code: Int = 0
        ) = Error<T>(message)
    }
}
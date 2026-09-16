package com.studyinfo.app.utils

/**
 * Lightweight Result type used across data layer.
 * Avoids mixing exceptions with normal control flow in our coroutines.
 */
sealed class AppResult<out T> {
    data class Success<T>(val value: T) : AppResult<T>()
    data class Failure(val message: String, val cause: Throwable? = null) : AppResult<Nothing>()

    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> value
        is Failure -> throw cause ?: IllegalStateException(message)
    }
}

inline fun <T, R> AppResult<T>.fold(
    onSuccess: (T) -> R,
    onFailure: (String, Throwable?) -> R,
): R = when (this) {
    is AppResult.Success -> onSuccess(value)
    is AppResult.Failure -> onFailure(message, cause)
}

fun <T> AppResult<T>.successOrNull(): T? = (this as? AppResult.Success)?.value

fun <T> success(value: T): AppResult<T> = AppResult.Success(value)
fun failure(message: String, cause: Throwable? = null): AppResult<Nothing> = AppResult.Failure(message, cause)

package com.sphe.base.extensions

import android.content.res.Resources
import androidx.annotation.StringRes
import com.andretietz.retroauth.AuthenticationCanceledException
import com.sphe.base.R
import com.sphe.models.error.EmptyResultException
import com.sphe.models.error.ValidationErrorException
import retrofit2.HttpException
import java.io.IOException

@StringRes
fun Throwable?.localizedTitle(): Int = when (this) {
    is EmptyResultException -> R.string.error_empty_title
    else -> R.string.error_title
}

@StringRes
fun Throwable?.localizedMessage(): Int = when (this) {
    is EmptyResultException -> R.string.error_empty
    is HttpException -> {
        when (code()) {
            404 -> R.string.error_notFound
            500 -> R.string.error_server
            502 -> R.string.error_keyError
            503 -> R.string.error_unavailable
            403, 401 -> R.string.error_auth
            else -> R.string.error_unknown
        }
    }
    is AuthenticationCanceledException -> R.string.error_noAuth
    is AppError -> messageRes
    is RuntimeException, is IOException -> R.string.error_network
    is ValidationErrorException -> error.errorRes

    else -> R.string.error_unknown
}



val localizedApiMessages = mapOf(
    "test" to R.string.error_errorLogOut
)

fun String.hasLocalizeApiMessage(): Boolean = localizedApiMessages.containsKey(this)

fun String.tryToLocalizeApiMessage(resources: Resources, overrideOnFail: Boolean = true): String = when {
    localizedApiMessages.containsKey(this) -> resources.getString(localizedApiMessages[this] ?: 0)
    else -> if (overrideOnFail) resources.getString(R.string.error_unknown) else this
}

data class ThrowableString(val value: String) : Throwable()

data class AppError(val messageRes: Int = R.string.error_unknown) : Throwable()
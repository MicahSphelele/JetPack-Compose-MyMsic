package com.sphe.base.utils

import android.content.Context
import java.util.*

//fun UiMessage<*>.asString(context: Context): String = when (this) {
//    is UiMessage.Plain -> value
//    is UiMessage.Resource -> context.getString(value, formatArgs)
//    is Error -> context.getString(value)
//}

sealed class UiMessage<T : Any>(open val value: T) {
    data class Plain(override val value: String) : UiMessage<String>(value)
    data class Resource(override val value: Int, val formatArgs: List<Any> = Collections.emptyList()) : UiMessage<Int>(value)
    data class Error(override val value: Throwable) : UiMessage<Throwable>(value)
}
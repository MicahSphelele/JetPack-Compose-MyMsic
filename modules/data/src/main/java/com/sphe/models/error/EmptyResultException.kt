package com.sphe.models.error

class EmptyResultException(override val message: String = "Result was empty") : RuntimeException(message)

fun <T> List<T>?.throwOnEmpty() = if (isNullOrEmpty()) throw EmptyResultException() else this

fun <T> Result<List<T>>.requireNonEmpty(condition: () -> Boolean = { true }): List<T> {
    return getOrThrow().apply { if (condition()) throwOnEmpty() }
}
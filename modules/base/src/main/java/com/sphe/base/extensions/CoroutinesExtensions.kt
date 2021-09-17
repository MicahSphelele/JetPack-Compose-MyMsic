package com.sphe.base.utils

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.concurrent.TimeUnit

fun <T, R> Flow<T?>.flatMapLatestNullable(transform: suspend (value: T) -> Flow<R>): Flow<R?> {
    return flatMapLatest { if (it != null) transform(it) else flowOf(null) }
}

fun <T, R> Flow<T?>.mapNullable(transform: suspend (value: T) -> R): Flow<R?> {
    return map { if (it != null) transform(it) else null }
}

fun <T> delayFlow(timeout: Long, value: T): Flow<T> = flow {
    delay(timeout)
    emit(value)
}

fun flowInterval(interval: Long, timeUnit: TimeUnit = TimeUnit.MILLISECONDS): Flow<Int> {
    val delayMillis = timeUnit.toMillis(interval)
    return channelFlow {
        var tick = 0
        send(tick)
        while (true) {
            delay(delayMillis)
            send(++tick)
        }
    }
}

fun <T> CoroutineScope.lazyAsync(block: suspend CoroutineScope.() -> T): Lazy<Deferred<T>> = lazy {
    async(start = CoroutineStart.LAZY) {
        block.invoke(this)
    }
}

/**
 * Alias to stateIn with defaults
 */
fun <T> Flow<T>.stateInDefault(
    scope: CoroutineScope,
    initialValue: T,
    started: SharingStarted = SharingStarted.WhileSubscribed(500),
) = stateIn(scope, started, initialValue)
package com.sphe.base.ui

import com.sphe.base.utils.delayFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.threeten.bp.Duration
import javax.inject.Inject

class SnackbarManager @Inject constructor() {
    private var maxDuration = Duration.ofSeconds(6).toMillis()
    private val maxQueue = 3

    private val pendingErrors = Channel<Throwable>(maxQueue, BufferOverflow.DROP_OLDEST)
    private val removeErrorSignal = Channel<Unit>(Channel.RENDEZVOUS)

    /**
     * A flow of [Throwable]s to display in the UI, usually as snackbars. The flow will immediately
     * emit `null`, and will then emit errors sent via [addError]. Once [maxDuration] has elapsed,
     * or [removeCurrentError] is called (if before that) `null` will be emitted to remove
     * the current error.
     */
    @ExperimentalCoroutinesApi
    val errors: Flow<Throwable?> = flow {
        emit(null)

        pendingErrors.receiveAsFlow().collect {
            emit(it)

            // Wait for either a maxDuration timeout, or a remove signal (whichever comes first)
            merge(
                delayFlow(maxDuration, Unit),
                removeErrorSignal.receiveAsFlow(),
            ).firstOrNull()

            // Remove the error
            emit(null)
        }
    }

    /**
     * Add [error] to the queue of errors to display.
     */
    suspend fun addError(error: Throwable) {
        pendingErrors.send(error)
    }

    /**
     * Remove the current error from being displayed.
     */
    suspend fun removeCurrentError() {
        removeErrorSignal.send(Unit)
    }
}
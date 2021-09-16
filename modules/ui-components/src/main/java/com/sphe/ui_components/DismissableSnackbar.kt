package com.sphe.ui_components

import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DismissableSnackbarHost(hostState: SnackbarHostState, modifier: Modifier = Modifier, onDismiss: () -> Unit = {}) {
    SnackbarHost(
        hostState = hostState,
        snackbar = {
            SwipeDismissSnackbar(
                data = it,
                onDismiss = onDismiss
            )
        },
        modifier = modifier
    )
}

/**
 * Wrapper around [Snackbar] to make it swipe-dismissable, using [SwipeToDismiss].
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeDismissSnackbar(
    data: SnackbarData,
    onDismiss: () -> Unit,
    snackbar: @Composable (SnackbarData) -> Unit = { Snackbar(it) }
) {
    Dismissable(onDismiss = onDismiss) {
        snackbar(data)
    }
}
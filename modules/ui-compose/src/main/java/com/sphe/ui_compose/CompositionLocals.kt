package com.sphe.ui_compose

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.staticCompositionLocalOf

val LocalScaffoldState = staticCompositionLocalOf<ScaffoldState> { error("No LocalScaffoldState provided") }

val LocalPlaybackConnection = staticCompositionLocalOf<Any> {
    error("No LocalPlaybackConnection provided")
}
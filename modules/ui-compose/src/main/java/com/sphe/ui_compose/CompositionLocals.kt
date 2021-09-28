package com.sphe.ui_compose

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.staticCompositionLocalOf
import com.sphe.models.playback.MediaSessionConnection

val LocalScaffoldState = staticCompositionLocalOf<ScaffoldState> { error("No LocalScaffoldState provided") }

val LocalPlaybackConnection = staticCompositionLocalOf<MediaSessionConnection> {
    error("No LocalPlaybackConnection provided")
}
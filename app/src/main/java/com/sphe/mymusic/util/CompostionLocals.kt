package com.sphe.mymusic.util

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.staticCompositionLocalOf
import com.sphe.mymusic.domain.PlaybackConnection

val LocalScaffoldState = staticCompositionLocalOf<ScaffoldState> { error("No LocalScaffoldState provided") }

val LocalPlaybackConnection = staticCompositionLocalOf<PlaybackConnection> {
    error("No LocalPlaybackConnection provided")
}
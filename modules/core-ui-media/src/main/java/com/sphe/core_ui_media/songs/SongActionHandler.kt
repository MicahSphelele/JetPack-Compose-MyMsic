package com.sphe.core_ui_media.songs

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import com.sphe.models.playback.MediaSessionConnection
import com.sphe.ui_compose.LocalPlaybackConnection

val LocalAudioActionHandler = staticCompositionLocalOf<SongActionHandler> {
    error("No LocalAudioActionHandler provided")
}

typealias SongActionHandler = (SongItemAction) -> Unit

@Composable
fun songActionHandler(
    playbackConnection: MediaSessionConnection = LocalPlaybackConnection.current,
    clipboardManager: ClipboardManager = LocalClipboardManager.current,
) : SongActionHandler {

    return { action ->
        when(action) {
            is SongItemAction.Play -> playbackConnection.playSong(song = action.song)
            is SongItemAction.PlayNext -> playbackConnection.playNextSong(song = action.song)
        }
    }
}
package com.sphe.core_ui_media.songs

import com.sphe.core_ui_media.R
import com.sphe.models.Song

sealed class SongActionItem(open val song: Song) {
    data class Play(override val song: Song) : SongActionItem(song)
    data class PlayNext(override val song: Song) : SongActionItem(song)

    companion object {
        fun from(actionLabelRes: Int, song: Song) = when (actionLabelRes) {
            R.string.audio_menu_play -> Play(song)
            R.string.audio_menu_playNext -> PlayNext(song)
            else -> error("Unknown action: $actionLabelRes")
        }
    }
}
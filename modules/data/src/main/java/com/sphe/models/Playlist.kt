package com.sphe.models

import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.sphe.models.constants.MusicServiceConstants.TYPE_PLAYLIST
import com.sphe.models.extension.value
import com.sphe.models.extension.valueOrEmpty
import kotlinx.parcelize.Parcelize

@Parcelize
data class Playlist(
    val id: Long,
    val name: String,
    val songCount: Int
) : MediaBrowserCompat.MediaItem(
    MediaDescriptionCompat.Builder()
        .setMediaId(MediaID(TYPE_PLAYLIST.toString(), id.toString()).asString())
        .setTitle(name)
        .setSubtitle("$songCount songs")
        .build(), FLAG_BROWSABLE
) {
    companion object {
        fun fromCursor(cursor: Cursor, songCount: Int): Playlist {
            return Playlist(
                id = cursor.value(MediaStore.Audio.Playlists._ID),
                name = cursor.valueOrEmpty(MediaStore.Audio.Playlists.NAME),
                songCount = songCount
            )
        }
    }
}

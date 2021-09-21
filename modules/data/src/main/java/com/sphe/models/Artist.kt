package com.sphe.models

import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.sphe.models.constants.MusicServiceConstants.TYPE_ARTIST
import com.sphe.models.extension.value
import kotlinx.parcelize.Parcelize

@Parcelize
data class Artist(
    var id: Long = 0,
    var name: String = "",
    var songCount: Int = 0,
    var albumCount: Int = 0
)  : MediaBrowserCompat.MediaItem(
    MediaDescriptionCompat.Builder()
        .setMediaId(MediaID(TYPE_ARTIST.toString(), id.toString()).asString())
        .setTitle(name)
        .setSubtitle("$albumCount albums")
        .build(), FLAG_BROWSABLE
) {

    companion object {
        fun fromCursor(cursor: Cursor): Artist {
            return Artist(
                id = cursor.value(MediaStore.Audio.Artists._ID),
                name = cursor.value(MediaStore.Audio.Artists.ARTIST),
                songCount = cursor.value(MediaStore.Audio.Artists.NUMBER_OF_TRACKS),
                albumCount = cursor.value(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)
            )
        }
    }
}

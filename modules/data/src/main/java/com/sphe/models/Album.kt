package com.sphe.models

import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.sphe.models.constants.MusicServiceConstants.TYPE_ALBUM
import com.sphe.models.constants.Utils
import com.sphe.models.extension.value
import com.sphe.models.extension.valueOrEmpty
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    var id: Long = 0,
    var title: String = "",
    var artist: String = "",
    var artistId: Long = 0,
    var songCount: Int = 0,
    var year: Int = 0
) : MediaBrowserCompat.MediaItem(
    MediaDescriptionCompat.Builder()
        .setMediaId(MediaID(TYPE_ALBUM.toString(), id.toString()).asString())
        .setTitle(title)
        .setIconUri(Utils.getAlbumArtUri(id))
        .setSubtitle(artist)
        .build(), FLAG_BROWSABLE
) {
    companion object {
        fun fromCursor(cursor: Cursor): Album {
            return Album(
                id = cursor.value(MediaStore.Audio.Albums._ID),
                title = cursor.valueOrEmpty(MediaStore.Audio.Albums.ALBUM),
                artist = cursor.valueOrEmpty(MediaStore.Audio.Albums.ARTIST),
                artistId = cursor.value(MediaStore.Audio.AudioColumns.ARTIST_ID),
                songCount = cursor.value(MediaStore.Audio.Albums.NUMBER_OF_SONGS),
                year = cursor.value(MediaStore.Audio.Albums.FIRST_YEAR)
            )
        }
    }



}
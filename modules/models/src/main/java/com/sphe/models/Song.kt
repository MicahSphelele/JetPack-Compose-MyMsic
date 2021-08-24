package com.sphe.models

import android.database.Cursor
import android.provider.MediaStore
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.sphe.models.constants.MusicServiceConstants
import com.sphe.models.constants.Utils
import com.sphe.models.extension.normalizeTrackNumber
import com.sphe.models.extension.value
import com.sphe.models.extension.valueOrDefault
import com.sphe.models.extension.valueOrEmpty
import kotlinx.parcelize.Parcelize

@Parcelize
data class Song(
    var id: Long = 0,
    var albumId: Long = 0,
    var artistId: Long = 0,
    var title: String = "",
    var artist: String = "",
    var album: String = "",
    var duration: Int = 0,
    var trackNumber: Int = 0
) : MediaBrowserCompat.MediaItem(MediaDescriptionCompat.Builder()
    .setMediaId(MediaID("${MusicServiceConstants.TYPE_SONG}", "$id").asString())
    .setTitle(title)
    .setIconUri(Utils.getAlbumArtUri(albumId))
    .setSubtitle(artist)
    .build(), FLAG_PLAYABLE
) {
    companion object {
        fun fromCursor(cursor: Cursor, albumId: Long = -1, artistId: Long = -1) : Song {
            return Song( id = cursor.value(MediaStore.Audio.Media._ID),
                 albumId = cursor.valueOrDefault(MediaStore.Audio.AudioColumns.ALBUM_ID, albumId),
             artistId = cursor.valueOrDefault(MediaStore.Audio.AudioColumns.ARTIST_ID, artistId),
             title = cursor.valueOrEmpty(MediaStore.Audio.AudioColumns.TITLE),
             artist  = cursor.valueOrEmpty(MediaStore.Audio.AudioColumns.ARTIST),
             album = cursor.valueOrEmpty(MediaStore.Audio.AudioColumns.ALBUM),
             duration = cursor.value(MediaStore.Audio.AudioColumns.DURATION),
             trackNumber  =cursor.value<Int>(MediaStore.Audio.AudioColumns.TRACK).normalizeTrackNumber()
            )
        }

        fun fromPlaylistMembersCursor(cursor: Cursor): Song {
            return Song(
                id = cursor.value(MediaStore.Audio.Playlists.Members.AUDIO_ID),
                albumId = cursor.value(MediaStore.Audio.AudioColumns.ALBUM_ID),
                artistId = cursor.value(MediaStore.Audio.AudioColumns.ARTIST_ID),
                title = cursor.valueOrEmpty(MediaStore.Audio.AudioColumns.TITLE),
                artist = cursor.valueOrEmpty(MediaStore.Audio.AudioColumns.ARTIST),
                album = cursor.valueOrEmpty(MediaStore.Audio.AudioColumns.ALBUM),
                duration = (cursor.value<Long>(MediaStore.Audio.AudioColumns.DURATION) / 1000).toInt(),
                trackNumber = cursor.value<Int>(MediaStore.Audio.AudioColumns.TRACK).normalizeTrackNumber()
            )
        }
    }
}

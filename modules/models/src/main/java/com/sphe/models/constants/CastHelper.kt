package com.sphe.models.constants

import android.util.Log
import androidx.core.net.toUri
import com.google.android.gms.cast.MediaInfo
import com.google.android.gms.cast.MediaInfo.STREAM_TYPE_BUFFERED
import com.google.android.gms.cast.MediaMetadata
import com.google.android.gms.cast.MediaMetadata.*
import com.google.android.gms.cast.MediaQueueItem
import com.google.android.gms.cast.MediaStatus
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.common.images.WebImage
import com.sphe.models.Song
import com.sphe.models.constants.Constants.PARAM_ID
import com.sphe.models.constants.Constants.PART_ALBUM_ART
import com.sphe.models.constants.Constants.PART_SONG
import java.net.MalformedURLException
import java.net.URL


object CastHelper {

    const val CAST_MUSIC_METADATA_ID = "metadata_id"
    const val CAST_MUSIC_METADATA_ALBUM_ID = "metadata_album_id"

    private const val CAST_URL_PROTOCOL = "http"
    private const val MIME_TYPE_AUDIO_MPEG = "audio/mpeg"

    fun castSong(castSession: CastSession, song: Song) {
        try {
            val remoteMediaClient = castSession.remoteMediaClient
            // TODO replace deprecated usage with correct usage
            remoteMediaClient?.load(song.toMediaInfo()!!, true, 0)
        } catch (e: Exception) {
            Log.d(Constants.TAG, "castSong failed", e)
        }
    }

    fun castSongQueue(castSession: CastSession, songs: List<Song>, currentPos: Int) {
        try {
            val remoteMediaClient = castSession.remoteMediaClient
            remoteMediaClient?.queueLoad(
                songs.toQueueInfoList(),
                currentPos,
                MediaStatus.REPEAT_MODE_REPEAT_OFF,
                0,
                null
            )
        } catch (e: Exception) {
            Log.d(Constants.TAG, "castSongQueue failed", e)
        }
    }

    private fun Song.toMediaInfo(): MediaInfo? {
        val song = this
        val ipAddress = Utils.getIPAddress(true)
        val baseUrl: URL

        try {
            baseUrl = URL(CAST_URL_PROTOCOL, ipAddress, Constants.CAST_SERVER_PORT, "")
        } catch (e: MalformedURLException) {
            Log.d(Constants.TAG, "MalformedURLException", e)
            return null
        }

        val songUrl = "$baseUrl/$PART_SONG?$PARAM_ID=${song.id}"
        val albumArtUrl = "$baseUrl/$PART_ALBUM_ART?$PARAM_ID=${song.albumId}"
        val musicMetadata = MediaMetadata(MEDIA_TYPE_MUSIC_TRACK).apply {
            putInt(CAST_MUSIC_METADATA_ID, song.id.toInt())
            putInt(CAST_MUSIC_METADATA_ALBUM_ID, song.albumId.toInt())
            putString(KEY_TITLE, song.title)
            putString(KEY_ARTIST, song.artist)
            putString(KEY_ALBUM_TITLE, song.album)
            putInt(KEY_TRACK_NUMBER, song.trackNumber)
            addImage(WebImage(albumArtUrl.toUri()))
        }

        return MediaInfo.Builder(songUrl).apply {
            setStreamType(STREAM_TYPE_BUFFERED)
            setContentType(MIME_TYPE_AUDIO_MPEG)
            setMetadata(musicMetadata)
            setStreamDuration(song.duration.toLong())
        }.build()
    }

    private fun List<Song>.toQueueInfoList(): Array<MediaQueueItem> {
        return map { MediaQueueItem.Builder(it.toMediaInfo()!!).build() }.toTypedArray()
    }
}
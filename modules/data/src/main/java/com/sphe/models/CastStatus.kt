package com.sphe.models

import com.google.android.gms.cast.MediaMetadata.*
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import com.sphe.models.constants.CastHelper

data class CastStatus(
    var isCasting: Boolean = false,
    var castDeviceName: String = "",
    var castSongId: Int = -1,
    var castAlbumId: Int = -1,
    var castSongTitle: String = "",
    var castSongAlbum: String = "",
    var castSongArtist: String = "",
    var state: Int = STATUS_NONE
) {
    companion object {
        const val STATUS_NONE = -1
        const val STATUS_PLAYING = 0
        const val STATUS_PAUSED = 1
        const val STATUS_BUFFERING = 2
    }

    fun fromRemoteMediaClient(deviceName: String, remoteMediaClient: RemoteMediaClient?) : CastStatus {
        remoteMediaClient ?: return this.apply { isCasting = false }
        castDeviceName = deviceName

        state = when {
            remoteMediaClient.isBuffering -> STATUS_BUFFERING
            remoteMediaClient.isPlaying -> STATUS_PLAYING
            remoteMediaClient.isPaused -> STATUS_PAUSED
            else -> STATUS_NONE
        }

        remoteMediaClient.currentItem?.media?.metadata?.let {
            castSongTitle = it.getString(KEY_TITLE)!!
            castSongArtist = it.getString(KEY_ARTIST)!!
            castSongAlbum = it.getString(KEY_ALBUM_TITLE)!!
            castSongId = it.getInt(CastHelper.CAST_MUSIC_METADATA_ID)
            castAlbumId = it.getInt(CastHelper.CAST_MUSIC_METADATA_ALBUM_ID)
        }
        isCasting = true

        return this
    }
}

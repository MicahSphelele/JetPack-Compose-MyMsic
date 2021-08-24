package com.sphe.models

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import com.sphe.models.constants.MusicServiceConstants
import com.sphe.models.constants.Utils
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
    .setMediaId(MediaID("${MusicServiceConstants.TYPE_SONG}", "$id").toString())
    .setTitle(title)
    .setIconUri(Utils.getAlbumArtUri(albumId))
    .setSubtitle(artist)
    .build(), FLAG_PLAYABLE
)
package com.sphe.models.extension

import android.app.Activity
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import com.sphe.models.constants.Constants
import com.sphe.models.constants.Constants.BY_UI_KEY
import com.sphe.models.constants.Constants.PAUSE_ACTION
import com.sphe.models.constants.Constants.PLAY_ACTION
import com.sphe.models.playback.QUEUE_CURRENT_INDEX
import com.sphe.models.playback.QUEUE_HAS_NEXT
import com.sphe.models.playback.QUEUE_HAS_PREVIOUS
import timber.log.Timber
import java.util.ArrayList

fun PlaybackStateCompat.position(): Long {
    return position
}

fun getMediaController(activity: Activity): MediaControllerCompat? {
    return MediaControllerCompat.getMediaController(activity)
}

fun getPlaybackState(activity: Activity): PlaybackStateCompat? {
    return getMediaController(activity)?.playbackState
}

fun isPlaying(activity: Activity): Boolean {
    return getMediaController(activity)?.playbackState?.state == PlaybackStateCompat.STATE_PLAYING
}

fun position(activity: Activity): Long? {
    return getMediaController(activity)?.playbackState?.position
}

fun duration(activity: Activity): Long? {
    return getMediaController(activity)?.metadata?.getLong(MediaMetadataCompat.METADATA_KEY_DURATION)
}

fun MediaSessionCompat.position(): Long {
    return controller.playbackState.position
}

fun MediaSessionCompat.isPlaying(): Boolean {
    return controller.playbackState.state == PlaybackStateCompat.STATE_PLAYING
}

fun getQueue(activity: Activity): LongArray? {
    return getMediaController(activity)?.queue?.toIDList()
}

fun getCurrentMediaID(activity: Activity): Long? {
    return getMediaController(activity)?.metadata?.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)?.toLong()
}

fun getExtraBundle(queue: LongArray, title: String): Bundle? {
    return getExtraBundle(queue, title, 0)
}

fun getExtraBundle(queue: LongArray, title: String, seekTo: Int?): Bundle? {
    val bundle = Bundle()
    bundle.putLongArray(Constants.SONGS_LIST, queue)
    bundle.putString(Constants.QUEUE_TITLE, title)
    if (seekTo != null)
        bundle.putInt(Constants.SEEK_TO_POS, seekTo)
    else bundle.putInt(Constants.SEEK_TO_POS, 0)
    return bundle
}

fun ArrayList<MediaBrowserCompat.MediaItem>.toRawMediaItems(): ArrayList<MediaBrowserCompat.MediaItem> {
    val list = arrayListOf<MediaBrowserCompat.MediaItem>()
    forEach {
        list.add(
            MediaBrowserCompat.MediaItem(
                MediaDescriptionCompat.Builder()
                    .setMediaId(it.description.mediaId)
                    .setTitle(it.description.title)
                    .setIconUri(it.description.iconUri)
                    .setSubtitle(it.description.subtitle)
                    .build(), it.flags))
    }
    return list
}

val NONE_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()

val NONE_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()

fun MediaControllerCompat.playPause() {
    playbackState?.let {
        when {
            it.isPlaying -> transportControls?.sendCustomAction(PAUSE_ACTION, bundleOf(BY_UI_KEY to false))
            it.isPlayEnabled -> transportControls?.sendCustomAction(PLAY_ACTION, bundleOf(BY_UI_KEY to false))
            else -> Timber.d("Couldn't play or pause the media controller")
        }
    }
}

fun MediaControllerCompat.toggleShuffleMode() {
    val new = when (shuffleMode) {
        PlaybackStateCompat.SHUFFLE_MODE_NONE -> PlaybackStateCompat.SHUFFLE_MODE_ALL
        PlaybackStateCompat.SHUFFLE_MODE_ALL -> PlaybackStateCompat.SHUFFLE_MODE_NONE
        else -> {
            Timber.e("Unknown shuffle mode $shuffleMode")
            return
        }
    }
    Timber.i("Toggling shuffle mode from=$shuffleMode, to=$new")
    transportControls.setShuffleMode(new)
}

fun MediaControllerCompat.toggleRepeatMode() {
    transportControls.setRepeatMode(
        when (repeatMode) {
            PlaybackStateCompat.REPEAT_MODE_NONE -> PlaybackStateCompat.REPEAT_MODE_ALL
            PlaybackStateCompat.REPEAT_MODE_ALL -> PlaybackStateCompat.REPEAT_MODE_ONE
            else -> PlaybackStateCompat.REPEAT_MODE_NONE
        }
    )
}

fun createDefaultPlaybackState(): PlaybackStateCompat.Builder {
    return PlaybackStateCompat.Builder().setActions(
        PlaybackStateCompat.ACTION_PLAY
                or PlaybackStateCompat.ACTION_PAUSE
                or PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH
                or PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                or PlaybackStateCompat.ACTION_PLAY_PAUSE
                or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                or PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE
                or PlaybackStateCompat.ACTION_SET_REPEAT_MODE
                or PlaybackStateCompat.ACTION_SEEK_TO
    )
}


fun MediaSessionCompat.isBuffering(): Boolean {
    return controller.playbackState.state == PlaybackStateCompat.STATE_BUFFERING
}

inline val MediaSessionCompat.repeatMode
    get() = controller.repeatMode

inline val MediaSessionCompat.shuffleMode
    get() = controller.shuffleMode

inline val Pair<PlaybackStateCompat, MediaMetadataCompat>.isActive
    get() = (first.state != PlaybackStateCompat.STATE_NONE && second != NONE_PLAYING)

inline val PlaybackStateCompat.isBuffering
    get() = (state == PlaybackStateCompat.STATE_BUFFERING)

inline val PlaybackStateCompat.isStopped
    get() = (state == PlaybackStateCompat.STATE_STOPPED)

inline val PlaybackStateCompat.isIdle
    get() = (state == PlaybackStateCompat.STATE_NONE || state == PlaybackStateCompat.STATE_STOPPED)

inline val PlaybackStateCompat.isError
    get() = (state == PlaybackStateCompat.STATE_ERROR)


inline val PlaybackStateCompat.currentIndex
    get() = (extras?.getInt(QUEUE_CURRENT_INDEX) ?: 0)

inline val PlaybackStateCompat.hasPrevious
    get() = (extras?.getBoolean(QUEUE_HAS_PREVIOUS) ?: false)

inline val PlaybackStateCompat.hasNext
    get() = (extras?.getBoolean(QUEUE_HAS_NEXT) ?: true)

inline val MediaMetadataCompat.id: String get() = getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)

inline val MediaMetadataCompat.title: String? get() = getString(MediaMetadataCompat.METADATA_KEY_TITLE)

inline val MediaMetadataCompat.artist: String? get() = getString(MediaMetadataCompat.METADATA_KEY_ARTIST)

inline val MediaMetadataCompat.duration: Long get() = getLong(MediaMetadataCompat.METADATA_KEY_DURATION)

inline val MediaMetadataCompat.album: String? get() = getString(MediaMetadataCompat.METADATA_KEY_ALBUM)

inline val MediaMetadataCompat.displayDescription: String? get() = getString(MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION)

inline val MediaMetadataCompat.artwork: Bitmap? get() = getBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART)
inline val MediaMetadataCompat.artworkUri: Uri get() = (getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI) ?: "").toUri()
package com.sphe.models.playback

import android.content.ComponentName
import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_AUDIO
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import com.sphe.models.MediaID
import com.sphe.models.QueueData
import com.sphe.models.Song
import com.sphe.models.constants.Constants.PLAY_NEXT
import com.sphe.models.repositories.SongsRepository

const val SEEK_TO = "action_seek_to"

const val QUEUE_MEDIA_ID_KEY = "queue_media_id_key"
const val QUEUE_TITLE_KEY = "queue_title_key"
const val QUEUE_LIST_KEY = "queue_list_key"

const val QUEUE_FROM_POSITION_KEY = "queue_from_position_key"
const val QUEUE_TO_POSITION_KEY = "queue_to_position_key"

interface MediaSessionConnection {
    val isConnected: MutableLiveData<Boolean>
    val rootMediaId: String
    val playbackState: MutableLiveData<PlaybackStateCompat>
    val nowPlaying: MutableLiveData<MediaMetadataCompat>
    val queueData: MutableLiveData<QueueData>
    val transportControls: MediaControllerCompat.TransportControls
    var mediaController: MediaControllerCompat

    fun playSong(song: Song)
    fun playNextSong(song: Song)
    fun playSongs(songs: List<Song>, index: Int)

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback)

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback)
}

class RealMediaSessionConnection(
    context: Context,
    serviceComponent: ComponentName,
    songsRepository: SongsRepository,
    queue: Queue
) : MediaSessionConnection {
    override val isConnected = MutableLiveData<Boolean>()
        .apply { postValue(false) }
    override val rootMediaId: String get() = mediaBrowser.root

    override val playbackState = MutableLiveData<PlaybackStateCompat>()
        .apply { postValue(EMPTY_PLAYBACK_STATE) }
    override val nowPlaying = MutableLiveData<MediaMetadataCompat>()
        .apply { postValue(NOTHING_PLAYING) }
    override val queueData = MutableLiveData<QueueData>()

    override lateinit var mediaController: MediaControllerCompat

    override fun playSong(song: Song) {
       playSongs(listOf(song), 0,)
    }

    override fun playNextSong(song: Song) {
        transportControls.sendCustomAction(
            PLAY_NEXT,
            bundleOf(
                QUEUE_MEDIA_ID_KEY to song.id
            )
        )
    }

    override fun playSongs(songs: List<Song>, index: Int) {
        val songIds = songs.map { it.id.toString() }.toTypedArray()
        val song = songs[index]

        transportControls.playFromMediaId(
            MediaID(MEDIA_TYPE_AUDIO.toString(), song.id.toString()).toString(),
            Bundle().apply {
                putStringArray(QUEUE_LIST_KEY, songIds)
                putString(QUEUE_TITLE_KEY, song.title)
            }
        )
    }

    override val transportControls: MediaControllerCompat.TransportControls
        get() = mediaController.transportControls

    private val mediaBrowserConnectionCallback = MediaBrowserConnectionCallback(context)
    private val mediaBrowser = MediaBrowserCompat(context,
        serviceComponent,
        mediaBrowserConnectionCallback, null)
        .apply { connect() }

    override fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.subscribe(parentId, callback)
    }

    override fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        mediaBrowser.unsubscribe(parentId, callback)
    }

    private inner class MediaBrowserConnectionCallback(private val context: Context)
        : MediaBrowserCompat.ConnectionCallback() {
        override fun onConnected() {
            mediaController = MediaControllerCompat(context, mediaBrowser.sessionToken).apply {
                registerCallback(MediaControllerCallback())
            }

            isConnected.postValue(true)
        }

        override fun onConnectionSuspended() {
            isConnected.postValue(false)
        }

        override fun onConnectionFailed() {
            isConnected.postValue(false)
        }
    }

    private inner class MediaControllerCallback : MediaControllerCompat.Callback() {
        override fun onPlaybackStateChanged(state: PlaybackStateCompat?) {
            playbackState.postValue(state ?: EMPTY_PLAYBACK_STATE)
        }

        override fun onMetadataChanged(metadata: MediaMetadataCompat?) {
            nowPlaying.postValue(metadata ?: NOTHING_PLAYING)
        }

        override fun onQueueChanged(queue: MutableList<MediaSessionCompat.QueueItem>?) {
            queueData.postValue(QueueData().fromMediaController(mediaController))
        }

        override fun onSessionDestroyed() {
            mediaBrowserConnectionCallback.onConnectionSuspended()
        }
    }
}

@Suppress("PropertyName")
val EMPTY_PLAYBACK_STATE: PlaybackStateCompat = PlaybackStateCompat.Builder()
    .setState(PlaybackStateCompat.STATE_NONE, 0, 0f)
    .build()

@Suppress("PropertyName")
val NOTHING_PLAYING: MediaMetadataCompat = MediaMetadataCompat.Builder()
    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, "")
    .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, 0)
    .build()
package com.sphe.models.playback

import android.content.ComponentName
import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaControllerCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.lifecycle.MutableLiveData
import com.sphe.models.QueueData

interface MediaSessionConnection {
    val isConnected: MutableLiveData<Boolean>
    val rootMediaId: String
    val playbackState: MutableLiveData<PlaybackStateCompat>
    val nowPlaying: MutableLiveData<MediaMetadataCompat>
    val queueData: MutableLiveData<QueueData>
    val transportControls: MediaControllerCompat.TransportControls

    var mediaController: MediaControllerCompat

    fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback)

    fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback)
}

class RealMediaSessionConnection(
    context: Context,
    serviceComponent: ComponentName
) : MediaSessionConnection {
    override val isConnected: MutableLiveData<Boolean>
        get() = TODO("Not yet implemented")
    override val rootMediaId: String
        get() = TODO("Not yet implemented")
    override val playbackState: MutableLiveData<PlaybackStateCompat>
        get() = TODO("Not yet implemented")
    override val nowPlaying: MutableLiveData<MediaMetadataCompat>
        get() = TODO("Not yet implemented")
    override val queueData: MutableLiveData<QueueData>
        get() = TODO("Not yet implemented")
    override val transportControls: MediaControllerCompat.TransportControls
        get() = TODO("Not yet implemented")
    override var mediaController: MediaControllerCompat
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun subscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        TODO("Not yet implemented")
    }

    override fun unsubscribe(parentId: String, callback: MediaBrowserCompat.SubscriptionCallback) {
        TODO("Not yet implemented")
    }

}
package com.sphe.models.playback

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.os.bundleOf
import com.sphe.models.Song
import com.sphe.models.constants.Constants.BY_UI_KEY

const val REPEAT_MODE = "repeat_mode"
const val SHUFFLE_MODE = "shuffle_mode"
const val QUEUE_CURRENT_INDEX = "queue_current_index"
const val QUEUE_HAS_PREVIOUS = "queue_has_previous"
const val QUEUE_HAS_NEXT = "queue_has_next"

const val DEFAULT_FORWARD_REWIND = 10 * 1000

typealias OnPrepared<T> = T.() -> Unit
typealias OnError<T> = T.(error: Throwable) -> Unit
typealias OnCompletion<T> = T.() -> Unit
typealias OnBuffering<T> = T.() -> Unit
typealias OnReady<T> = T.() -> Unit
typealias OnMetaDataChanged = MyMusicPlayer.() -> Unit
typealias OnIsPlaying<T> = T.(playing: Boolean, byUi: Boolean) -> Unit

interface MyMusicPlayer {
    fun getSession(): MediaSessionCompat
    fun playAudio(extras: Bundle = bundleOf(BY_UI_KEY to true))
    suspend fun playSong(id: String, index: Int? = null)
    suspend fun playSong(song: Song, index: Int? = null)
    fun seekTo(position: Long)
    fun fastForward()
    fun rewind()
    fun pause(extras: Bundle = bundleOf(BY_UI_KEY to true))
    suspend fun nextAudio(): String?
    suspend fun repeatAudio()
    suspend fun repeatQueue()
    suspend fun previousAudio()
    fun playNext(id: String)
    suspend fun skipTo(position: Int)
    fun removeFromQueue(position: Int)
    fun removeFromQueue(id: String)
    fun swapQueueAudios(from: Int, to: Int)
    fun stop(byUser: Boolean = true)
    fun release()
    fun onPlayingState(playing: OnIsPlaying<MyMusicPlayer>)
    fun onPrepared(prepared: OnPrepared<MyMusicPlayer>)
    fun onError(error: OnError<MyMusicPlayer>)
    fun onCompletion(completion: OnCompletion<MyMusicPlayer>)
    fun onMetaDataChanged(metaDataChanged: OnMetaDataChanged)
    fun updatePlaybackState(applier: PlaybackStateCompat.Builder.() -> Unit = {})
    fun setPlaybackState(state: PlaybackStateCompat)
    fun updateData(list: List<String> = emptyList(), title: String? = null)
    fun setData(list: List<String> = emptyList(), title: String? = null)
    suspend fun setDataFromMediaId(_mediaId: String, extras: Bundle = bundleOf())
    suspend fun saveQueueState()
    suspend fun restoreQueueState()
    fun clearRandomAudioPlayed()
    fun setCurrentAudioId(audioId: String, index: Int? = null)
    fun shuffleQueue(isShuffle: Boolean)
}
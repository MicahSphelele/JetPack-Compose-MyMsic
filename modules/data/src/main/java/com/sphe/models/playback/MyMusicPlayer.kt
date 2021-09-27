package com.sphe.models.playback

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.util.PriorityTaskManager
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

const val REPEAT_MODE = "repeat_mode"
const val SHUFFLE_MODE = "shuffle_mode"
const val QUEUE_CURRENT_INDEX = "queue_current_index"
const val QUEUE_HAS_PREVIOUS = "queue_has_previous"
const val QUEUE_HAS_NEXT = "queue_has_next"

const val DEFAULT_FORWARD_REWIND = 10 * 1000

typealias OnPrepared<T> = T.() -> Unit
typealias OnError<T> = T.(error: Throwable) -> Unit
typealias OnCompletion<T> = T.() -> Unit

interface MyMusicPlayer {
    fun play()

    fun setSource(path: String): Boolean

    fun setSource(uri: Uri): Boolean

    fun prepare()

    fun seekTo(position: Int)

    fun isPrepared(): Boolean

    fun isPlaying(): Boolean

    fun position(): Int

    fun pause()

    fun stop()

    fun reset()

    fun release()

    fun onPrepared(prepared: OnPrepared<MyMusicPlayer>)

    fun onError(error: OnError<MyMusicPlayer>)

    fun onCompletion(completion: OnCompletion<MyMusicPlayer>)
}

@Singleton
class RealMyMusicPlayer @Inject constructor(@ApplicationContext private val context: Context) :
    MyMusicPlayer, Player.Listener {

    private var isPrepared = false

    private var playerBase: ExoPlayer? = null
    private val player: ExoPlayer
        get() {
            if (playerBase == null) {
                playerBase = createPlayer(this)
            }
            return playerBase ?: throw IllegalStateException("Could not create an audio player")
        }

    private var onPrepared: OnPrepared<MyMusicPlayer> = {}
    private var onError: OnError<MyMusicPlayer> = {}
    private var onCompletion: OnCompletion<MyMusicPlayer> = {}

    override fun play() {
        player.play()
    }

    override fun setSource(path: String): Boolean {
        Timber.d("setSource() - $path")
        try {
            player.setMediaItem(MediaItem.fromUri(path))
            //player.setDataSource(path)
        } catch (e: Exception) {
            Timber.d("setSource() - failed")
            onError(this, e)
            return false
        }
        return true
    }

    override fun setSource(uri: Uri): Boolean {
        Timber.d("setSource() - $uri")
        try {
            player.setMediaItem(MediaItem.fromUri(uri))
            //player.setDataSource(path)
        } catch (e: Exception) {
            Timber.d("setSource() - failed")
            onError(this, e)
            return false
        }
        return true
    }

    override fun prepare() {
        player.prepare()
    }

    override fun seekTo(position: Int) {
        player.seekTo(position.toLong())
    }

    override fun isPrepared(): Boolean = isPrepared

    override fun isPlaying(): Boolean = player.isPlaying

    override fun position(): Int = player.currentPosition.toInt()

    override fun pause() {
        player.pause()
    }

    override fun stop() {
        player.stop()
    }

    override fun reset() {
        player.playWhenReady = false
    }

    override fun release() {
        player.release()
    }

    override fun onPrepared(prepared: OnPrepared<MyMusicPlayer>) {
        this.onPrepared = prepared
    }

    override fun onError(error: OnError<MyMusicPlayer>) {
        this.onError = error
    }

    override fun onCompletion(completion: OnCompletion<MyMusicPlayer>) {
        this.onCompletion = completion
    }

    private fun createPlayer(owner: RealMyMusicPlayer): ExoPlayer {
        return SimpleExoPlayer.Builder(
            context,
            DefaultRenderersFactory(context).apply {
                setExtensionRendererMode(DefaultRenderersFactory.EXTENSION_RENDERER_MODE_PREFER)
            }
        )
            .setLoadControl(object : DefaultLoadControl() {
                override fun onPrepared() {
                    isPrepared = true
                    onPrepared(owner)
                }
            })
            .build().apply {
                val attr = AudioAttributes.Builder().apply {
                    setContentType(C.CONTENT_TYPE_MUSIC)
                    setUsage(C.USAGE_MEDIA)
                }.build()

                setAudioAttributes(attr, false)
                setPriorityTaskManager(PriorityTaskManager())
                addListener(owner)
            }
    }
}
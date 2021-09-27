package com.sphe.models.playback

import android.os.Bundle
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import com.sphe.models.MediaID
import com.sphe.models.constants.Constants.ACTION_PLAY_NEXT
import com.sphe.models.constants.Constants.ACTION_QUEUE_REORDER
import com.sphe.models.constants.Constants.ACTION_REPEAT_QUEUE
import com.sphe.models.constants.Constants.ACTION_REPEAT_SONG
import com.sphe.models.constants.Constants.ACTION_RESTORE_MEDIA_SESSION
import com.sphe.models.constants.Constants.ACTION_SET_MEDIA_STATE
import com.sphe.models.constants.Constants.ACTION_SONG_DELETED
import com.sphe.models.constants.Constants.QUEUE_FROM
import com.sphe.models.constants.Constants.QUEUE_TITLE
import com.sphe.models.constants.Constants.QUEUE_TO
import com.sphe.models.constants.Constants.SEEK_TO_POS
import com.sphe.models.constants.Constants.SONG
import com.sphe.models.constants.Constants.SONGS_LIST
import com.sphe.models.db.dao.QueueDao
import com.sphe.models.repositories.SongsRepository

class MediaSessionCallback(private val mediaSession: MediaSessionCompat,
                           private val songPlayer: SongPlayer,
                           private val songsRepository: SongsRepository,
                           private val queueDao: QueueDao
) : MediaSessionCompat.Callback() {

    override fun onPause() = songPlayer.pause()

    override fun onPlay() = songPlayer.playSong()

    override fun onPlayFromSearch(query: String?, extras: Bundle?) {
        query?.let {
            val song = songsRepository.searchSongs(query, 1)
            if (song.isNotEmpty()) {
                songPlayer.playSong(song.first())
            }
        } ?: onPlay()
    }

    override fun onPlayFromMediaId(mediaId: String, extras: Bundle?) {
        val songId = MediaID().fromString(mediaId).mediaId!!.toLong()
        songPlayer.playSong(songId)

        if (extras == null) return
        val queue = extras.getLongArray(SONGS_LIST)
        val seekTo = extras.getInt(SEEK_TO_POS)
        val queueTitle = extras.getString(QUEUE_TITLE) ?: ""

        if (queue != null) {
            songPlayer.setQueue(queue, queueTitle)
        }
        if (seekTo > 0) {
            songPlayer.seekTo(seekTo)
        }
    }

    override fun onSeekTo(pos: Long) = songPlayer.seekTo(pos.toInt())

    override fun onSkipToNext() = songPlayer.nextSong()

    override fun onSkipToPrevious() = songPlayer.previousSong()

    override fun onStop() = songPlayer.stop()

    override fun onSetRepeatMode(repeatMode: Int) {
        super.onSetRepeatMode(repeatMode)
        val bundle = mediaSession.controller.playbackState.extras ?: Bundle()
        songPlayer.setPlaybackState(
            PlaybackStateCompat.Builder(mediaSession.controller.playbackState)
                .setExtras(bundle.apply {
                    putInt(REPEAT_MODE, repeatMode)
                }
                ).build()
        )
    }

    override fun onSetShuffleMode(shuffleMode: Int) {
        super.onSetShuffleMode(shuffleMode)
        val bundle = mediaSession.controller.playbackState.extras ?: Bundle()
        songPlayer.setPlaybackState(
            PlaybackStateCompat.Builder(mediaSession.controller.playbackState)
                .setExtras(bundle.apply {
                    putInt(SHUFFLE_MODE, shuffleMode)
                }).build()
        )
    }

    override fun onCustomAction(action: String?, extras: Bundle?) {
        when (action) {
            ACTION_SET_MEDIA_STATE -> setSavedMediaSessionState()
            ACTION_REPEAT_SONG -> songPlayer.repeatSong()
            ACTION_REPEAT_QUEUE -> songPlayer.repeatQueue()

            ACTION_PLAY_NEXT -> {
                val nextSongId = extras!!.getLong(SONG)
                songPlayer.playNext(nextSongId)
            }

            ACTION_QUEUE_REORDER -> {
                val from = extras!!.getInt(QUEUE_FROM)
                val to = extras.getInt(QUEUE_TO)
                songPlayer.swapQueueSongs(from, to)
            }

            ACTION_SONG_DELETED -> {
                val id = extras!!.getLong(SONG)
                songPlayer.removeFromQueue(id)
            }
            ACTION_RESTORE_MEDIA_SESSION -> restoreMediaSession()
        }
    }

    private fun setSavedMediaSessionState() {
        // Only set saved session from db if we know there is not any active media session
        val controller = mediaSession.controller ?: return
        if (controller.playbackState == null || controller.playbackState.state == PlaybackStateCompat.STATE_NONE) {
            val queueData = queueDao.getQueueDataSync() ?: return
            songPlayer.restoreFromQueueData(queueData)
        } else {
            // Force update the playback state and metadata from the media session so that the
            // attached Observer in NowPlayingViewModel gets the current state.
            restoreMediaSession()
        }
    }

    private fun restoreMediaSession() {
        songPlayer.setPlaybackState(mediaSession.controller.playbackState)
        mediaSession.setMetadata(mediaSession.controller.metadata)
    }
}
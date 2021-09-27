package com.sphe.models.playback

import android.app.Application
import android.content.Context
import android.support.v4.media.session.MediaSessionCompat
import com.sphe.models.Song
import com.sphe.models.db.dao.QueueDao
import com.sphe.models.repositories.SongsRepository
import java.util.*

interface Queue {

    var ids: LongArray
    var title: String
    var currentSongId: Long
    val currentSongIndex: Int

    val previousSongId: Long?
    val nextSongIndex: Int?
    val nextSongId: Long?

    fun setMediaSession(session: MediaSessionCompat)

    fun swap(from: Int, to: Int)

    fun moveToNext(id: Long)

    fun firstId(): Long

    fun lastId(): Long

    fun remove(id: Long)

    fun asQueueItems(): List<MediaSessionCompat.QueueItem>

    fun currentSong(): Song

    fun ensureCurrentId()

    fun reset()
}

class RealQueue(
    private val context: Context,
    private val songsRepository: SongsRepository,
    private val queueDao: QueueDao,
) : Queue {

    private lateinit var session: MediaSessionCompat
    private val shuffleRandom = Random()
    private val previousShuffles = mutableListOf<Int>()

    override var ids: LongArray
        get() = TODO("Not yet implemented")
        set(value) {}
    override var title: String
        get() = TODO("Not yet implemented")
        set(value) {}
    override var currentSongId: Long
        get() = TODO("Not yet implemented")
        set(value) {}
    override val currentSongIndex: Int
        get() = TODO("Not yet implemented")
    override val previousSongId: Long?
        get() = TODO("Not yet implemented")
    override val nextSongIndex: Int?
        get() = TODO("Not yet implemented")
    override val nextSongId: Long?
        get() = TODO("Not yet implemented")

    override fun setMediaSession(session: MediaSessionCompat) {
        TODO("Not yet implemented")
    }

    override fun swap(from: Int, to: Int) {
        TODO("Not yet implemented")
    }

    override fun moveToNext(id: Long) {
        TODO("Not yet implemented")
    }

    override fun firstId(): Long {
        TODO("Not yet implemented")
    }

    override fun lastId(): Long {
        TODO("Not yet implemented")
    }

    override fun remove(id: Long) {
        TODO("Not yet implemented")
    }

    override fun asQueueItems(): List<MediaSessionCompat.QueueItem> {
        TODO("Not yet implemented")
    }

    override fun currentSong(): Song {
        TODO("Not yet implemented")
    }

    override fun ensureCurrentId() {
        TODO("Not yet implemented")
    }

    override fun reset() {
        TODO("Not yet implemented")
    }

}
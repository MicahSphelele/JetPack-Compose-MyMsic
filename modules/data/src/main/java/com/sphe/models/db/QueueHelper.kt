package com.sphe.models.db

import com.sphe.models.db.dao.QueueDao
import com.sphe.models.db.entities.QueueEntity
import com.sphe.models.repositories.SongsRepository

interface QueueHelper {

    fun updateQueueSongs(
        queueSongs: LongArray?,
        currentSongId: Long?
    )

    fun updateQueueData(queueData: QueueEntity)
}

class RealQueueHelper(
    private val queueDao: QueueDao,
    private val songsRepository: SongsRepository
) : QueueHelper {
    override fun updateQueueSongs(queueSongs: LongArray?, currentSongId: Long?) {
        TODO("Not yet implemented")
    }

    override fun updateQueueData(queueData: QueueEntity) {
        TODO("Not yet implemented")
    }

}
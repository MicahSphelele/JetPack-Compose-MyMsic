package com.sphe.models.db

import com.sphe.models.db.dao.QueueDao
import com.sphe.models.db.entities.QueueEntity
import com.sphe.models.extension.equalsBy
import com.sphe.models.extension.toSongEntityList
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
        if (queueSongs == null || currentSongId == null) {
            return
        }
        val currentList = queueDao.getQueueSongsSync()
        val songListToSave = queueSongs.toSongEntityList(songsRepository)

        val listsEqual = currentList.equalsBy(songListToSave) { left, right ->
            left.id == right.id
        }
        if (queueSongs.isNotEmpty() && !listsEqual) {
            queueDao.clearQueueSongs()
            queueDao.insertAllSongs(songListToSave)
            setCurrentSongId(currentSongId)
        } else {
            setCurrentSongId(currentSongId)
        }
    }

    override fun updateQueueData(queueData: QueueEntity) =
        queueDao.insert(queueData)


    private fun setCurrentSongId(id: Long) = queueDao.setCurrentId(id)

}
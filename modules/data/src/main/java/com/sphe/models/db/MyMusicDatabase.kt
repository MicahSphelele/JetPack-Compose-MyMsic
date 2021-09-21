package com.sphe.models.db

import androidx.room.Database
import com.sphe.models.db.dao.QueueDao
import com.sphe.models.db.entities.QueueEntity
import com.sphe.models.db.entities.SongEntity

@Database(entities = [QueueEntity::class, SongEntity::class], version = 1, exportSchema = false)
abstract class MyMusicDatabase {

    abstract fun queueDao(): QueueDao
}
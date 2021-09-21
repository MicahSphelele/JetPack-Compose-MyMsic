package com.sphe.models.db

import android.content.Context
import androidx.room.Room
import com.sphe.models.db.dao.QueueDao
import com.sphe.models.repositories.SongsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun providesDatabase(@ApplicationContext context: Context) : MyMusicDatabase =
        Room.databaseBuilder(context, MyMusicDatabase::class.java,"mymusic.db")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun providesQueueDao(myMusicDatabase: MyMusicDatabase) : QueueDao =
        myMusicDatabase.queueDao()

    @Provides
    fun providesQueueHelper(queueDao: QueueDao, songsRepository: SongsRepository): QueueHelper =
        RealQueueHelper(queueDao,songsRepository)


}
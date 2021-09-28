package com.sphe.models.playback

import android.content.ComponentName
import android.content.Context
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
class MediaModule {

    @Provides
    fun providesQueue(
        @ApplicationContext context: Context,
        songsRepository: SongsRepository,
        queueDao: QueueDao
    ): Queue = RealQueue(context, songsRepository, queueDao)

    @Provides
    fun providesMyMusicPlayer(@ApplicationContext context: Context): MyMusicPlayer =
        RealMyMusicPlayer(context)

    @Provides
    fun providesSongPlayer(
        @ApplicationContext context: Context,
        myMusicPlayer: MyMusicPlayer,
        songsRepository: SongsRepository,
        queueDao: QueueDao,
        queue: Queue
    ): SongPlayer =
        RealSongPlayer(context, myMusicPlayer, songsRepository, queueDao, queue)

    @Singleton
    @Provides
    fun providesSessionPlayback(
        @ApplicationContext context: Context,
        songsRepository: SongsRepository
    ): MediaSessionConnection =
        RealMediaSessionConnection(
            context,
            ComponentName(context, MyMusicService::class.java),
            songsRepository
        )


}
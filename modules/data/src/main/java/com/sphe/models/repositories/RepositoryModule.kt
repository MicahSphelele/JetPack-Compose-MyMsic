package com.sphe.models.repositories

import android.content.Context
import com.sphe.models.enums.SongSortOrder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {

    @Provides
    fun providesSongsRepository(@ApplicationContext  context: Context) : SongsRepository =
        RealSongsRepository(context.contentResolver, SongSortOrder.SONG_A_Z)

}
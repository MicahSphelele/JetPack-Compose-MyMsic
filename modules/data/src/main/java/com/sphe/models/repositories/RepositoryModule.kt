package com.sphe.models.repositories

import android.content.Context
import com.sphe.models.enums.AlbumSortOrder
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
    fun providesSongsRepository(@ApplicationContext context: Context): SongsRepository =
        RealSongsRepository(context.contentResolver, SongSortOrder.SONG_A_Z)

    @Provides
    fun providesAlbumRepository(@ApplicationContext context: Context): AlbumRepository =
        RealAlbumRepository(context.contentResolver, AlbumSortOrder.ALBUM_A_Z)

    @Provides
    fun providesArtistRepository(@ApplicationContext context: Context): ArtistRepository =
        RealArtistRepository(context.contentResolver)

    @Provides
    fun providesGenreRepository(@ApplicationContext context: Context): GenreRepository =
        RealGenreRepository(context.contentResolver)

    @Provides
    fun providesPlaylistRepository(@ApplicationContext context: Context): PlaylistRepository =
        RealPlaylistRepository(context.contentResolver)

    @Provides
    fun providesFolderRepository(@ApplicationContext context: Context): FoldersRepository =
        RealFoldersRepository()



}
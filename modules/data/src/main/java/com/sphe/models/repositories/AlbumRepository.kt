package com.sphe.models.repositories

import android.content.ContentResolver
import com.sphe.models.Album
import com.sphe.models.Song
import com.sphe.models.enums.AlbumSortOrder

interface AlbumRepository {

    fun getAllAlbums(caller: String?): List<Album>

    fun getAlbum(id: Long): Album

    fun getAlbums(paramString: String, limit: Int): List<Album>

    fun getSongsForAlbum(albumId: Long, caller: String?): List<Song>

    fun getAlbumsForArtist(artistId: Long): List<Album>
}

class RealAlbumRepository(
    private val contentResolver: ContentResolver,
    private val sortOrderPref: AlbumSortOrder
) : AlbumRepository {
    override fun getAllAlbums(caller: String?): List<Album> {
        TODO("Not yet implemented")
    }

    override fun getAlbum(id: Long): Album {
        TODO("Not yet implemented")
    }

    override fun getAlbums(paramString: String, limit: Int): List<Album> {
        TODO("Not yet implemented")
    }

    override fun getSongsForAlbum(albumId: Long, caller: String?): List<Song> {
        TODO("Not yet implemented")
    }

    override fun getAlbumsForArtist(artistId: Long): List<Album> {
        TODO("Not yet implemented")
    }

}
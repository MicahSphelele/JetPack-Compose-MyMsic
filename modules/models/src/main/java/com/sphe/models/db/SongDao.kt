package com.sphe.models.db

import android.content.ContentResolver
import androidx.paging.PagingSource
import com.sphe.models.Album
import com.sphe.models.Song
import com.sphe.models.db.base.PaginatedEntryDao
import kotlinx.coroutines.flow.Flow

class SongDao(private val contentResolver: ContentResolver): PaginatedEntryDao<Album, Song>()  {

    override fun entries(): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun entriesObservable(count: Int, offset: Int): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override fun entriesPagingSource(): PagingSource<Int, Song> {
        TODO("Not yet implemented")
    }

    override fun entry(id: String): Flow<Song> {
        TODO("Not yet implemented")
    }

    override fun entryNullable(id: String): Flow<Song?> {
        TODO("Not yet implemented")
    }

    override fun entriesById(ids: List<String>): Flow<List<Song>> {
        TODO("Not yet implemented")
    }

    override suspend fun has(id: String): Int {
        TODO("Not yet implemented")
    }

    override fun entriesPagingSource(params: Album): PagingSource<Int, Song> {
        TODO("Not yet implemented")
    }

    override suspend fun count(params: Album): Int {
        TODO("Not yet implemented")
    }

    override fun entriesObservable(params: Album, page: Int): Flow<List<Song>> {
        TODO("Not yet implemented")
    }


}
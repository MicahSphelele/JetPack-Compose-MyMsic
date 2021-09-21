package com.sphe.models.db.base

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.MediaStore
import androidx.paging.PagingSource
import com.sphe.models.Song
import com.sphe.models.db.base.PaginatedEntryDao
import com.sphe.models.extension.mapList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SongDao(private val contentResolver: ContentResolver) : PaginatedEntryDao<String, Song>() {

    @SuppressLint("Recycle")
    override fun entries(): Flow<List<Song>> = flow {

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection =
            arrayOf("_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id")

        val cursor = contentResolver
            .query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null, null,
            )

        if (cursor == null) {
            emit(emptyList())
        }

        if (cursor?.count == 0) {
            emit(emptyList())
        }

        if (cursor?.count!! > 0) {

            val list = cursor.mapList(closeAfter = true) {
                Song.fromCursor(this)
            }
            emit(list)
        }
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

    override fun entriesPagingSource(params: String): PagingSource<Int, Song> {
        TODO("Not yet implemented")
    }

    override suspend fun count(params: String): Int {
        TODO("Not yet implemented")
    }

    override fun entriesObservable(params: String, page: Int): Flow<List<Song>> {
        TODO("Not yet implemented")
    }


}
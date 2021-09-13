package com.sphe.models.db

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.provider.MediaStore
import com.sphe.models.Song
import com.sphe.models.enums.SongSortOrder
import com.sphe.models.extension.mapList

class MusicDatabase(private val contentResolver: ContentResolver) {

    @SuppressLint("Recycle")
    suspend fun getPaginatedSongList(
        sortOrder: SongSortOrder,
        limit: Int,
        offset: Int
    ): List<Song> {

        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        val projection =
            arrayOf("_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id")

        var list = emptyList<Song>()

        val cursor = contentResolver
            .query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null, "$sortOrder LIMIT $limit OFFSET $offset",
            ) ?: return emptyList()

        if (cursor.count < 0) {
            return emptyList()
        }

        return cursor.mapList(closeAfter = true) { Song.fromCursor(this) }

    }
}
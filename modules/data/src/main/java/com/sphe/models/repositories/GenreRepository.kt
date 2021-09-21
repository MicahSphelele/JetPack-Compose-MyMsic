package com.sphe.models.repositories

import android.content.ContentResolver
import android.database.Cursor
import android.provider.MediaStore
import com.sphe.models.Genre
import com.sphe.models.MediaID
import com.sphe.models.Song
import com.sphe.models.extension.mapList
import com.sphe.models.extension.value

interface GenreRepository {

    fun getAllGenres(caller: String?): List<Genre>

    fun getSongsForGenre(genreId: Long, caller: String?): List<Song>

}

class RealGenreRepository(
    private val contentResolver: ContentResolver
) : GenreRepository {
    override fun getAllGenres(caller: String?): List<Genre> {
        MediaID.currentCaller = caller
        return makeGenreCursor().mapList(true) {
            val id: Long = value(MediaStore.Audio.Genres._ID)
            val songCount = getSongCountForGenre(id)
            Genre.fromCursor(this, songCount)
        }.filter { it.songCount > 0 }
    }

    override fun getSongsForGenre(genreId: Long, caller: String?): List<Song> {
        MediaID.currentCaller = caller
        return makeGenreSongCursor(genreId)
            .mapList(true) { Song.fromCursor(this) }
    }
    private fun makeGenreCursor(): Cursor? {
        val uri = MediaStore.Audio.Genres.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Audio.Genres._ID, MediaStore.Audio.Genres.NAME)
        return contentResolver.query(uri, projection, null, null, MediaStore.Audio.Genres.NAME)
    }

    private fun getSongCountForGenre(genreID: Long): Int {
        val uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreID)
        return contentResolver.query(uri, null, null, null, null)?.use {
            it.moveToFirst()
            if (it.count == 0) {
                -1
            } else {
                it.count
            }
        } ?: -1
    }

    private fun makeGenreSongCursor(genreID: Long): Cursor? {
        val uri = MediaStore.Audio.Genres.Members.getContentUri("external", genreID)
        val projection = arrayOf("_id", "title", "artist", "album", "duration", "track", "album_id", "artist_id")
        return contentResolver.query(uri, projection, null, null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
    }
}
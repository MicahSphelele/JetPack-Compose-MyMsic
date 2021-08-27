package com.sphe.mymusic.presentation

import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import com.sphe.models.Song
import com.sphe.models.enums.SongSortOrder
import com.sphe.models.extension.mapList
import com.sphe.models.utils.MusicUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            // A surface container using the 'background' color from the theme
            Surface(color = MaterialTheme.colors.background) {
                Greeting("Android")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.IO).launch {
            val songs = makeSongCursor()
                .mapList(true) { Song.fromCursor(this) }
            Log.e("@APP","${songs.size}")
            songs.forEach {
                val songUri = MusicUtils.getSongUri(it.id)

                Log.e("@APP","${MusicUtils.getRealPathFromURI(this@MainActivity, songUri)}")
            }
        }
    }

    private fun makeSongCursor(): Cursor {
        return makeSongCursor(SongSortOrder.SONG_Z_A.rawValue)!!
    }

    private fun makeSongCursor(
        sortOrder: String?
    ): Cursor? {
        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
        //val selectionStatement = StringBuilder("is_music = 1")
//        if (!selection.isNullOrEmpty()) {
//            selectionStatement.append("AND $selection")
//        }
        val projection =
            arrayOf("_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id")

        return contentResolver
            .query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null, null
            )
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Greeting("Android")
}
package com.sphe.mymusic.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat

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

    @SuppressLint("LogNotTimber")
    override fun onStart() {
        super.onStart()
//        CoroutineScope(Dispatchers.IO).launch {
//            val songs = makeSongCursor()
//                .mapList(true) { Song.fromCursor(this) }
//            Log.e("@APP", "${songs.size}")
//            songs.forEach {
//                val songUri = MusicUtils.getSongUri(it.id)
//                Log.e("@APP","Artist: ${it.artist} Song: ${it.title}")
//            }
//        }
    }

//    private fun makeSongCursor(): Cursor {
//        return makeSongCursor(SongSortOrder.SONG_Z_A.rawValue)!!
//    }
//
//    private fun makeSongCursor(
//        sortOrder: String?
//    ): Cursor? {
//        val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
//        val projection =
//            arrayOf("_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id")
//
//        return contentResolver
//            .query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                projection,
//                selection,
//                null,  "$sortOrder LIMIT 5 OFFSET 1",
//            )
//    }
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
package com.sphe.ui_compose.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.*
import androidx.navigation.compose.NamedNavArgument
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import com.sphe.models.Artist

interface Screen {
    val route: String
}

const val ARTIST_ID_KEY = "artist_id"
const val ALBUM_ID_KEY = "album_id"

sealed class RootScreen(
    override val route: String,
    val startScreen: LeafScreen,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList()
) : Screen {
    object SongScreen: RootScreen("songs_root", LeafScreen.SongScreen)
    object ArtistScreen: RootScreen("artists_root", LeafScreen.ArtistScreen)
    object AlbumScreen: RootScreen("albums_root", LeafScreen.AlbumScreen)
    object PlaylistScreen: RootScreen("playlist_root",LeafScreen.PlaylistScreen)
    object SettingsScreen: RootScreen("settings_root",LeafScreen.SettingsScreen)
}

sealed class LeafScreen(
    override val route: String,
    val arguments: List<NamedNavArgument> = emptyList(),
    val deepLinks: List<NavDeepLink> = emptyList()
) : Screen {
    object SongScreen: LeafScreen("songs")
    object ArtistScreen: LeafScreen("artists")
    object ArtistDetailsScreen: LeafScreen("artist_details",
        arguments = listOf(navArgument(ARTIST_ID_KEY) {
        type =  NavType.LongType
    })
    ) {
        fun buildRoute(artist: Artist) = "artists/${artist.id}"
    }
    object AlbumScreen: LeafScreen("albums")
    object AlbumDetailsScreen: LeafScreen("album_details",
        arguments = listOf(navArgument(ALBUM_ID_KEY) {
            type =  NavType.LongType
        })
    ) {
        fun buildRoute(artist: Artist) = "artists/${artist.id}"
    }
    object PlaylistScreen: LeafScreen("playlists")
    object SettingsScreen: LeafScreen("settings")
}

fun NavGraphBuilder.composableScreen(screen: LeafScreen, content: @Composable (NavBackStackEntry) -> Unit) =
    composable(screen.route, screen.arguments, screen.deepLinks, content)

@Composable
inline fun <reified VM : ViewModel> NavBackStackEntry.scopedViewModel(navController: NavController): VM {
    val parentId = destination.parent!!.id
    val parentBackStackEntry = navController.getBackStackEntry(parentId)
    return hiltViewModel(parentBackStackEntry)
}
package com.sphe.ui_compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.sphe.ui_compose.collectEvent
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class)
@Composable
internal fun AppNavigation(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current
) {
    collectEvent(flow = navigator.queue) { event ->
        when(event) {
            is NavigationEvent.Destination -> navController.navigate(event.route)
            is NavigationEvent.Back -> navController.navigateUp()
            else -> Unit
        }
    }
}

private fun NavGraphBuilder.addSongsRoot(navController: NavController) {
    this.navigation(
        route = RootScreen.SongScreen.route,
        startDestination = LeafScreen.SongScreen.route
    ) {

    }
}
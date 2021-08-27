package com.sphe.ui_compose.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class)
@Composable
internal fun AppNavigation(
    navController: NavController,
    navigator: Navigator = LocalNavigator.current
) {

}
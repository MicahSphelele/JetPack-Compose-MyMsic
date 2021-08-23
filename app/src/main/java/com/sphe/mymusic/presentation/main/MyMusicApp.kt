package com.sphe.mymusic.presentation.main

import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.google.accompanist.insets.ProvideWindowInsets
import com.sphe.mymusic.util.LocalScaffoldState

@Composable
fun MyMusicApp(scaffoldState: ScaffoldState = rememberScaffoldState()) {
    CompositionLocalProvider(LocalScaffoldState provides scaffoldState) {
        ProvideWindowInsets(consumeWindowInsets = false) {
            MyMusicAppCore {

            }
        }
    }
}

@Composable
fun MyMusicAppCore(content: @Composable () -> Unit) {



}
package com.sphe.ui_settings

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import com.sphe.ui_compose.rememberFlowWithLifecycle
import com.sphe.ui_theme.ThemeViewModel

val LocalAppVersion = staticCompositionLocalOf { "Unknown" }

@Composable
fun Settings(
    themeViewModel: ThemeViewModel = hiltViewModel(),
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    val settingsLinks by rememberFlowWithLifecycle(viewModel.settingsLinks).collectAsState(emptyList())
    
}
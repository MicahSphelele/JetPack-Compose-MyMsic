package com.sphe.ui_settings

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.sphe.base.ui.ColorPalettePreference
import com.sphe.base.ui.DarkModePreference
import com.sphe.base.ui.ThemeState
import com.sphe.ui_components.components.AppTopBar
import com.sphe.ui_components.components.SelectableDropdownMenu
import com.sphe.ui_compose.rememberFlowWithLifecycle
import com.sphe.ui_theme.ThemeViewModel
import com.sphe.ui_theme.theme.AppTheme

val LocalAppVersion = staticCompositionLocalOf { "Unknown" }

@Composable
fun Settings(
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val themeState by rememberFlowWithLifecycle(themeViewModel.themeState).collectAsState(initial = null)

    //val settingsLinks by rememberFlowWithLifecycle(viewModel.settingsLinks).collectAsState(emptyList())

    themeState?.let { theme ->
        Settings(themeState = theme, setThemeState = themeViewModel::applyThemeState)
    }

}

@Composable
private fun Settings(
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit
) {
    Scaffold(topBar = {
        AppTopBar(title = stringResource(R.string.settings_title))
    }) { padding ->
        SettingsList(themeState = themeState, setThemeState = setThemeState, paddings = padding)
    }
}

@Composable
fun SettingsList(
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit,
    paddings: PaddingValues
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(AppTheme.specs.padding),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = paddings
    ) {
        settingsThemeSection(themeState, setThemeState)
        //settingsDownloadsSection()
        settingsAboutSection()
        //settingsLinksSection(settingsLinks)
    }
}

fun LazyListScope.settingsThemeSection(
    themeState: ThemeState,
    setThemeState: (ThemeState) -> Unit
) {
    item {
        SettingsSectionLabel(stringResource(R.string.settings_theme))
        SettingsItem(stringResource(R.string.settings_theme_darkMode)) {
            SelectableDropdownMenu(
                items = DarkModePreference.values().toList(),
                selectedItem = themeState.darkModePreference,
                onItemSelect = { setThemeState(themeState.copy(darkModePreference = it)) },
            )
        }
        SettingsItem(stringResource(R.string.settings_theme_colorPalette)) {
            SelectableDropdownMenu(
                items = ColorPalettePreference.values().toList(),
                selectedItem = themeState.colorPalettePreference,
                onItemSelect = { setThemeState(themeState.copy(colorPalettePreference = it)) }
            )
        }
    }
}

fun LazyListScope.settingsAboutSection() {
    item {
        SettingsSectionLabel(stringResource(R.string.settings_about))

        Column(verticalArrangement = Arrangement.spacedBy(AppTheme.specs.padding)) {
            SettingsLinkItem(
                labelRes = R.string.settings_about_author,
                textRes = R.string.settings_about_author_text,
                linkRes = R.string.settings_about_author_link
            )

            SettingsLinkItem(
                label = stringResource(R.string.settings_about_version),
                text = LocalAppVersion.current,
                link = stringResource(R.string.play_store_url)
            )
        }
    }
}

@Composable
private fun SettingsSectionLabel(text: String) {
    Text(
        text, style = MaterialTheme.typography.h6,
        color = MaterialTheme.colors.secondary,
        modifier = Modifier.padding(AppTheme.specs.inputPaddings)
    )
}

@Composable
private fun SettingsLinkItem(
    @StringRes labelRes: Int,
    @StringRes textRes: Int,
    @StringRes linkRes: Int,
) {
    SettingsLinkItem(stringResource(labelRes), stringResource(textRes), stringResource(linkRes))
}

@Composable
private fun SettingsLinkItem(
    label: String,
    text: String,
    link: String
) {
    SettingsItem(label, verticalAlignment = Alignment.Top) {
        val context = LocalContext.current
        ClickableText(
            text = buildAnnotatedString { append(text) },
            style = TextStyle.Default.copy(
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.End
            ),
            onClick = {
                //analytics.event("settings.linkClick", mapOf("link" to link))
                //IntentUtils.openUrl(context, link)
            }
        )
    }
}

@Composable
private fun SettingsItem(
    label: String,
    modifier: Modifier = Modifier,
    labelWeight: Float = 1f,
    contentWeight: Float = 1f,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    content: @Composable () -> Unit = {},
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = verticalAlignment,
        modifier = modifier
            .padding(horizontal = AppTheme.specs.padding)
            .fillMaxWidth()
    ) {
        Text(
            label,
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier
                .padding(end = AppTheme.specs.paddingTiny)
                .weight(labelWeight)
        )
        Box(
            modifier = Modifier.weight(contentWeight, false),
            contentAlignment = Alignment.CenterEnd
        ) { content() }
    }
}
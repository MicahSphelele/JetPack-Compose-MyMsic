package com.sphe.ui_theme

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sphe.base.storge.PreferencesStore
import com.sphe.base.ui.ThemeState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val handle: SavedStateHandle,
    private val preferences: PreferencesStore,
) : ViewModel() {
    //val themeState = preferences.get(PreferenceKeys.THEME_STATE_KEY, ThemeState.serializer(), DefaultTheme)

    fun applyThemeState(themeState: ThemeState) {
//        viewModelScope.launch {
//            preferences.save(PreferenceKeys.THEME_STATE_KEY, themeState, ThemeState.serializer())
//        }
    }
}
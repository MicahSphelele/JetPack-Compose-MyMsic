package com.sphe.mymusic.presentation.viewmodels

import androidx.lifecycle.ViewModel
import com.sphe.mymusic.util.storage.PreferencesStore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(private val preferences: PreferencesStore) : ViewModel() {
    //val themeState = preferences.get(AppConstants.THEME_STATE_KEY, ThemeState.serializer(), null)
}
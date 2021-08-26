package com.sphe.mymusic.presentation.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor() : ViewModel() {
    //val themeState = preferences.get(AppConstants.THEME_STATE_KEY, ThemeState.serializer(), null)
}
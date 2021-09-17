package com.sphe.ui_settings

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(handle: SavedStateHandle) : ViewModel() {

    val settingsLink = flow {
        emit("This is a settings link")
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed())
}
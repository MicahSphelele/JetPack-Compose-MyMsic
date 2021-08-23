package com.sphe.mymusic.util

import kotlinx.serialization.json.Json
object AppConstants {
    const val THEME_STATE_KEY = "theme_state"
    const val STORE_NAME = "app_preferences"
    val DEFAULT_JSON_FORMAT = Json {
        ignoreUnknownKeys = true
    }
}
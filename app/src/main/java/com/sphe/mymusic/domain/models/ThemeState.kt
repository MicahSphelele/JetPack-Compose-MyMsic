package com.sphe.mymusic.domain.models

import android.os.Parcelable
import com.sphe.mymusic.domain.ColorPalettePreference
import com.sphe.mymusic.domain.DarkModePreference
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Parcelize
@Serializable
data class ThemeState(
    @SerialName("darkMode")
    var darkModePreference: DarkModePreference = DarkModePreference.AUTO,
    @SerialName("colorPalette")
    var colorPalettePreference: ColorPalettePreference = ColorPalettePreference.Default
) : Parcelable {

    val isDarkMode get() = darkModePreference == DarkModePreference.ON
}
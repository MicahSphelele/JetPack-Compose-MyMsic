package com.sphe.ui_theme.theme

import androidx.compose.material.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.sphe.ui_theme.R

private val APP_FONT = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.circular_black,
            weight = FontWeight.Black,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.circular_bold,
            weight = FontWeight.Bold,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.circular_regular,
            weight = FontWeight.Normal,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.montserrat_light,
            weight = FontWeight.Light,
            style = FontStyle.Normal
        ),
    )
)

val Typography = Typography(defaultFontFamily = APP_FONT)
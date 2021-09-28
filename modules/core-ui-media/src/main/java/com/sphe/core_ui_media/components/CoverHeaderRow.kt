package com.sphe.core_ui_media.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.sphe.base.base.imageloading.ImageLoading
import com.sphe.ui_components.components.ImageWithPlaceholder
import com.sphe.ui_components.gradientBackground
import com.sphe.ui_theme.theme.AppTheme
import com.sphe.ui_theme.theme.parseColor
import com.sphe.ui_theme.theme.textShadow


object CoverHeaderDefaults {
    val height = 300.dp

    val titleStyle
        @Composable get() = MaterialTheme.typography.h4.copy(
            color = Color.White,
            shadow = textShadow(),
            fontWeight = FontWeight.Black
        )

    val overlayGradient = Modifier.gradientBackground(
        0.2f to parseColor("#10000000"),
        1.0f to parseColor("#00000000"),
        angle = 80f
    )
}

@ExperimentalCoilApi
@Composable
fun CoverHeaderRow(
    title: String,
    modifier: Modifier = Modifier,
    imageRequest: Any? = null,
    height: Dp = CoverHeaderDefaults.height,
    titleStyle: TextStyle = CoverHeaderDefaults.titleStyle,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        val painter = rememberImagePainter(imageRequest, builder = ImageLoading.defaultConfig)

        ImageWithPlaceholder(painter = painter, modifier = Modifier.height(height)) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = it.fillMaxWidth()
            )
        }

        Box(
            Modifier
                .height(height)
                .fillMaxWidth()
                .then(CoverHeaderDefaults.overlayGradient)
        )

        Text(
            text = title,
            style = titleStyle,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(AppTheme.specs.padding)
        )

    }
}
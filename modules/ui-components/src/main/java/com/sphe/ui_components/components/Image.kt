package com.sphe.ui_components.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.ImagePainter
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.PlaceholderDefaults
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.color
import com.google.accompanist.placeholder.material.fade
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.shimmer

@ExperimentalCoilApi
@Composable
fun ImageWithPlaceholder(
    painter: ImagePainter,
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    errorOrEmpty: @Composable () -> Unit = {},
    image: @Composable (Modifier) -> Unit
) {
    Box(modifier = modifier) {
        image(
            Modifier
                .fillMaxSize()
                .placeholder(
                    visible = painter.state is ImagePainter.State.Loading,
                    shape = shape,
                    highlight = PlaceholderHighlight.fade()
                )
        )

        when (painter.state) {
            is ImagePainter.State.Error, ImagePainter.State.Empty -> {
                errorOrEmpty()
            }
            else -> Unit
        }
    }
}

@ExperimentalCoilApi
@Composable
fun CoverImage(
    painter: ImagePainter,
    modifier: Modifier = Modifier,
    size: Dp = Dp.Unspecified,
    backgroundColor: Color = PlaceholderDefaults.color(),
    contentColor: Color = MaterialTheme.colors.secondary,
    contentScale: ContentScale = ContentScale.FillBounds,
    shape: Shape = MaterialTheme.shapes.small,
    icon: VectorPainter = rememberVectorPainter(Icons.Default.MusicNote),
    iconPadding: Dp = if (size != Dp.Unspecified) size * 0.25f else 24.dp,
    bitmapPlaceholder: Bitmap? = null,
    image: @Composable (Modifier) -> Unit
) {
    val state = painter.state
    val sizeMod = if (size != Dp.Unspecified) Modifier.size(size) else Modifier
    Surface(
        elevation = 2.dp,
        shape = shape,
        color = backgroundColor,
        modifier = modifier
            .then(sizeMod)
            .aspectRatio(1f)
    ) {
        image(
            Modifier
                .fillMaxSize()
                .placeholder(
                    visible = state is ImagePainter.State.Loading,
                    color = backgroundColor,
                    shape = shape,
                    highlight = PlaceholderHighlight.shimmer(highlightColor = contentColor.copy(alpha = .15f)),
                )
        )

        when (state) {
            is ImagePainter.State.Error, ImagePainter.State.Empty, is ImagePainter.State.Loading -> {
                Icon(
                    painter = icon,
                    tint = contentColor.copy(alpha = ContentAlpha.disabled),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(iconPadding)
                )
            }
            else -> Unit
        }

        if (bitmapPlaceholder != null && state is ImagePainter.State.Loading) {
            Image(
                painter = rememberImagePainter(bitmapPlaceholder),
                contentDescription = null,
                contentScale = contentScale,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape)
            )
        }
    }
}
package com.sphe.core_ui_media.artists

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.material.placeholder
import com.sphe.base.base.imageloading.ImageLoading
import com.sphe.models.Artist
import com.sphe.models.utils.MusicUtils
import com.sphe.ui_components.components.CoverImage
import com.sphe.ui_components.components.shimmer
import com.sphe.ui_theme.theme.AppTheme

object ArtistsDefaults {
    val imageSize = 70.dp
    val nameWidth = 100.dp
}

@ExperimentalCoilApi
@Composable
fun ArtistColumn(
    artist: Artist,
    imageSize: Dp = ArtistsDefaults.imageSize,
    nameWidth: Dp = ArtistsDefaults.nameWidth,
    isPlaceHolder: Boolean = false,
    onClick: (Artist) -> Unit = {}
) {
    val loadingModifier = Modifier.placeholder(visible = isPlaceHolder, highlight = shimmer())

    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
        horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
            .clickable {
                if (!isPlaceHolder) onClick(artist)
            }
            .fillMaxWidth()
            .padding(AppTheme.specs.paddingTiny)
    ) {
        val bitmap = MusicUtils.getAlbumArtBitmap(LocalContext.current, artist.albumId)
        val image = rememberImagePainter(bitmap, builder = ImageLoading.defaultConfig)

        CoverImage(
            painter = image,
            icon = rememberVectorPainter(Icons.Default.Person),
            shape = CircleShape,
            size = imageSize
        ) { modifier ->
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = modifier.then(loadingModifier)
            )
        }

        Text(
            artist.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .width(nameWidth)
                .then(loadingModifier)
        )

    }

}
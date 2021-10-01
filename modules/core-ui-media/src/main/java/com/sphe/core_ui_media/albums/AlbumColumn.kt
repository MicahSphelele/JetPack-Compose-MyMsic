package com.sphe.core_ui_media.albums

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.material.placeholder
import com.sphe.base.base.imageloading.ImageLoading
import com.sphe.models.Album
import com.sphe.models.utils.MusicUtils
import com.sphe.ui_components.components.CoverImage
import com.sphe.ui_components.components.shimmer
import com.sphe.ui_theme.theme.AppTheme

object AlbumsDefaults {
    val imageSize = 150.dp
}

@ExperimentalCoilApi
@Composable
fun AlbumColumn(
    album: Album,
    modifier: Modifier = Modifier,
    imageSize: Dp = AlbumsDefaults.imageSize,
    isPlaceholder: Boolean = false,
    onClick: (Album) -> Unit = {},
) {
    val loadingModifier = Modifier.placeholder(
        visible = isPlaceholder,
        highlight = shimmer(),
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingSmall),
        modifier = modifier
            .clickable {
                if (!isPlaceholder) onClick(album)
            }
            .fillMaxWidth()
            .padding(AppTheme.specs.padding)
    ) {
        val bitmap = MusicUtils.getAlbumArtBitmap(LocalContext.current, album.id)
        val image = rememberImagePainter(bitmap, builder = ImageLoading.defaultConfig)

        CoverImage(
            painter = image,
            size = imageSize,
            icon = rememberVectorPainter(Icons.Default.Album),
        ) { modifier ->
            Image(
                painter = image,
                contentDescription = null,
                modifier = modifier.then(loadingModifier)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier.width(imageSize)
        ) {
            Text(album.title, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = loadingModifier, style = MaterialTheme.typography.body1)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    album.artist,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = loadingModifier,
                    style = MaterialTheme.typography.body2
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingTiny),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    //if (album.explicit)
//                        Icon(
//                            painter = rememberVectorPainter(Icons.Filled.Explicit),
//                            contentDescription = null,
//                            modifier = Modifier.size(16.dp),
//                            tint = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium),
//                        )
                    Text(album.year.toString(), modifier = loadingModifier, style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}
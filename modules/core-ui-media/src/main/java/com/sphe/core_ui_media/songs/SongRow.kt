package com.sphe.core_ui_media.songs

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.accompanist.placeholder.material.placeholder
import com.sphe.models.Song
import com.sphe.models.extension.interpunctize
import com.sphe.models.extension.millisToDuration
import com.sphe.models.utils.MusicUtils
import com.sphe.ui_components.components.CoverImage
import com.sphe.ui_components.components.shimmer
import com.sphe.ui_theme.theme.AppTheme

object SongsDefaults {
    val imageSize = 48.dp
    const val maxLines = 3
}

@ExperimentalCoilApi
@Composable
fun SongRow(
    song: Song,
    modifier: Modifier = Modifier,
    imageSize: Dp = SongsDefaults.imageSize,
    isPlaceholder: Boolean = false,
    onClick: ((Song) -> Unit)? = null,
    onPlayAudio: ((Song) -> Unit)? = null,
    actionHandler: SongActionHandler = LocalAudioActionHandler.current
) {
    var menuVisible by remember { mutableStateOf(false) }
    val contentScaleOnMenuVisible = animateFloatAsState((if (menuVisible) 0.97f else 1f))

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clickable {
                if (!isPlaceholder)
                    if (onClick != null) onClick(song)
                    else menuVisible = true
            }
            .fillMaxWidth()
            .padding(AppTheme.specs.inputPaddings)
    ) {
        SongRowItem(
            song = song,
            isPlaceholder = isPlaceholder,
            imageSize = imageSize,
            onCoverClick = {
                if (onPlayAudio != null) onPlayAudio(song)
                else actionHandler(SongItemAction.Play(song))
            },
            modifier = Modifier
                .weight(19f)
                .graphicsLayer {
                    scaleX *= contentScaleOnMenuVisible.value
                    scaleY *= contentScaleOnMenuVisible.value
                }
        )
    }
}

@ExperimentalCoilApi
@Composable
fun SongRowItem(
    song: Song,
    modifier: Modifier = Modifier,
    imageSize: Dp = SongsDefaults.imageSize,
    onCoverClick: (Song) -> Unit = {},
    isPlaceholder: Boolean = false,
    maxLines: Int = SongsDefaults.maxLines,
) {
    val bitmap = MusicUtils.getAlbumArtBitmap(LocalContext.current, song.albumId)


    val loadingModifier = Modifier.placeholder(
        visible = isPlaceholder,
        highlight = shimmer(),
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.padding),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        val image = rememberImagePainter(data = bitmap)

        CoverImage(
            painter = image,
            size = imageSize,
        ) { imageMod ->
            Image(
                painter = image,
                contentDescription = null,
                modifier = imageMod
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onCoverClick(song) },
                    )
                    .then(loadingModifier)
            )
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingTiny)) {
        Text(
            song.title,
            style = MaterialTheme.typography.body2.copy(fontSize = 15.sp),
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
            modifier = loadingModifier
        )
        CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingTiny),
                verticalAlignment = Alignment.CenterVertically,
            ) {
//                if (song.explicit)
//                    Icon(
//                        painter = rememberVectorPainter(Icons.Filled.Explicit),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(18.dp)
//                            .alignByBaseline(),
//                        tint = MaterialTheme.colors.onBackground.copy(alpha = ContentAlpha.medium),
//                    )
                val artistAndDuration = listOf(song.artist, song.durationMillis().millisToDuration()).interpunctize()
                Text(
                    artistAndDuration,
                    style = MaterialTheme.typography.subtitle2.copy(fontSize = 14.sp),
                    maxLines = maxLines,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .alignByBaseline()
                        .then(loadingModifier)
                )
            }
        }
    }
}
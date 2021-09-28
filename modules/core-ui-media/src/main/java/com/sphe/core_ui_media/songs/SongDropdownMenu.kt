package com.sphe.core_ui_media.songs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import com.sphe.core_ui_media.R

private val defaultMenuActionLabels = listOf(
    R.string.audio_menu_play,
    R.string.audio_menu_playNext
)

@Composable
fun SongDropdownMenu(
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    actionLabels: List<Int> = defaultMenuActionLabels,
    onDropdownSelect: (Int) -> Unit = {}
) {
    IconButton(
        onClick = { onExpandedChange(true) },
        modifier = modifier
    ) {
        Icon(
            painter = rememberVectorPainter(Icons.Default.MoreVert),
            contentDescription = stringResource(R.string.audio_menu_cd),
        )
    }

    Box {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .align(Alignment.Center)
        ) {
            actionLabels.forEach { item ->
                val label = stringResource(item)
                DropdownMenuItem(
                    onClick = {
                        onExpandedChange(false)
                        onDropdownSelect(item)
                    }
                ) {
                    Text(text = label)
                }
            }
        }
    }
}
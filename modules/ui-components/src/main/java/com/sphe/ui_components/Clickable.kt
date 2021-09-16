package com.sphe.ui_components

import android.R.attr
import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import androidx.compose.material.ripple.rememberRipple


@Composable
fun Modifier.simpleClickable(
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    indication: Indication? = null,
    onClick: () -> Unit,
) = clickable(
    onClick = onClick, role = Role.Button,
    indication = indication,
    interactionSource = interactionSource
)

@Composable
fun Modifier.coloredRippleClickable(
    onClick: () -> Unit,
    color: Color = MaterialTheme.colors.secondary,
    bounded: Boolean = false,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    rippleRadius: Dp = 24.dp,
) = clickable(
    onClick = onClick, role = Role.Button,
    indication = rememberRipple(color = color, bounded = bounded, radius = rippleRadius),
    interactionSource = interactionSource
)
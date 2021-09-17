package com.sphe.ui_components.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.sphe.ui_components.R
import com.sphe.ui_components.Zoomable
import com.sphe.ui_components.colorFilterDynamicProperty
import com.sphe.ui_theme.theme.AppTheme

@Composable
fun EmptyErrorBox(
    modifier: Modifier = Modifier,
    message: String = stringResource(R.string.error_empty),
    retryVisible: Boolean = true,
    onRetryClick: () -> Unit = {},
) {

}

@Preview
@Composable
fun ErrorBox(
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.error_title),
    message: String = stringResource(R.string.error_unknown),
    retryVisible: Boolean = true,
    onRetryClick: () -> Unit = {}
) {
    val loadingYOffset = 130.dp
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxWidth()
            .offset(y = loadingYOffset / 3f)
    ) {
        val wavesComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.waves_animation))

        Zoomable {
            LottieAnimation(
                wavesComposition,
                speed = 0.5f,
                iterations = LottieConstants.IterateForever,
                dynamicProperties = colorFilterDynamicProperty(MaterialTheme.colors.secondary.copy(alpha = 0.1f)),
                modifier = Modifier.offset(y = -loadingYOffset)
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(AppTheme.specs.paddingTiny),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(top = AppTheme.specs.paddingLarge)
        ) {
            Text(title, style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold))
            Text(message)
            if (retryVisible)
                TextRoundedButton(
                    onClick = onRetryClick,
                    text = stringResource(R.string.error_retry),
                    modifier = Modifier.padding(top = AppTheme.specs.padding)
                )
        }
    }
}
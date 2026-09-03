package com.guidetrade.app.ui.theme.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class OrbState {
    Idle,
    Listening,
    Processing,
    Speaking,
    Error
}

@Composable
fun OrbAnimation(
    state: OrbState,
    modifier: Modifier = Modifier,
    onTap: () -> Unit = {}
) {
    val targetColor by animateColorAsState(
        targetValue = when (state) {
            OrbState.Idle -> Color(0xFF6200EE)
            OrbState.Listening -> Color(0xFF03DAC5)
            OrbState.Processing -> Color(0xFFFF9800)
            OrbState.Speaking -> Color(0xFF4CAF50)
            OrbState.Error -> Color(0xFFFF5252)
        },
        animationSpec = tween(durationMillis = 500)
    )

    val pulseScale by animateFloatAsState(
        targetValue = when (state) {
            OrbState.Idle -> 1f
            OrbState.Listening -> 1.05f
            OrbState.Processing -> 1.1f
            OrbState.Speaking -> 1.15f
            OrbState.Error -> 1.0f
        },
        animationSpec = tween(durationMillis = 500)
    )

    val ringAlpha by animateFloatAsState(
        targetValue = when (state) {
            OrbState.Idle -> 0.3f
            OrbState.Listening -> 0.6f
            OrbState.Processing -> 0.9f
            OrbState.Speaking -> 0.8f
            OrbState.Error -> 0.5f
        },
        animationSpec = tween(durationMillis = 500)
    )

    val glowAlpha by animateFloatAsState(
        targetValue = when (state) {
            OrbState.Idle -> 0.4f
            OrbState.Processing -> 0.8f
            OrbState.Speaking -> 0.6f
            OrbState.Listening -> 0.5f
            OrbState.Error -> 0.6f
        },
        animationSpec = tween(durationMillis = 500)
    )

    Box(
        modifier = modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(targetColor.copy(alpha = glowAlpha))
    ) {
        Canvas(modifier = Modifier.size(80.dp).clip(CircleShape)) {
            drawCircle(
                color = targetColor,
                radius = size.minDimension / 2 * pulseScale
            )
        }

        repeat(3) {
            val ringAlphaValue = ringAlpha * (1f - it * 0.2f)
            val ringSize = 80f + it * 20f
            Canvas(
                modifier = Modifier
                    .size(ringSize.dp)
                    .clip(CircleShape)
            ) {
                drawCircle(
                    color = targetColor.copy(alpha = ringAlphaValue),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = 2.dp.toPx()
                    )
                )
            }
        }
    }
}

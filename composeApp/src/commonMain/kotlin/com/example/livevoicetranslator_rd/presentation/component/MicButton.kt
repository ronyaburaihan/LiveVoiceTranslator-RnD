package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_mic
import org.jetbrains.compose.resources.painterResource

@Composable
fun MicButton(
    color: Color = PrimaryColor,
    shadowColor: Color = Color(0x4D0252FF),
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onLongClickRelease: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(52.dp),
        contentAlignment = Alignment.Center
    ) {
        val density = LocalDensity.current
        val blurRadius = with(density) { 20.dp.toPx() }
        val shadowOffsetY = with(density) { 4.dp.toPx() }
        val buttonRadius = with(density) { 26.dp.toPx() }

        Canvas(modifier = Modifier.fillMaxSize()) {
            val shadowCenter = center.copy(y = center.y + shadowOffsetY)
            val shadowRadius = buttonRadius + blurRadius
            
            val shadowBrush = Brush.radialGradient(
                colors = listOf(shadowColor, Color.Transparent),
                center = shadowCenter,
                radius = shadowRadius
            )
            
            drawCircle(
                brush = shadowBrush,
                radius = shadowRadius,
                center = shadowCenter
            )
        }

        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = this.center
                val innerRadius = 23.dp.toPx()
                val outerRingRadius = 25.dp.toPx()
                val outerRingWidth = 2.dp.toPx()

                drawCircle(
                    color = color,
                    radius = innerRadius,
                    center = center
                )

                drawCircle(
                    color = color,
                    radius = outerRingRadius,
                    center = center,
                    style = Stroke(width = outerRingWidth)
                )
            }

            Icon(
                painter = painterResource(Res.drawable.ic_mic),
                contentDescription = "Voice Input",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
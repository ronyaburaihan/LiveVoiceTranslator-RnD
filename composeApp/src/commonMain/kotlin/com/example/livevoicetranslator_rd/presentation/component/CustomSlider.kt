package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.BackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.headerBrush

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomSlider(
    modifier: Modifier = Modifier,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
    progress: Float,
    thumbSize: Dp = 24.dp,
    thumbShape: Shape = CircleShape,
    onProgressChange: (Float) -> Unit,
    progressBarUnFill: Brush? = null,
    progressBarFill: ProgressBarFill = ProgressBarFill.Gradient(headerBrush),
    unfilledColor: Color = BackgroundColor,
    trackHeight: Dp = 15.dp,
) {
    val fillBrush: Brush = when (progressBarFill) {
        is ProgressBarFill.Solid -> SolidColor(progressBarFill.color)
        is ProgressBarFill.Gradient -> progressBarFill.brush
    }


    Slider(
        value = progress,
        onValueChange = { newValue ->
            //  isDragging = true
            onProgressChange(newValue)
        },
        onValueChangeFinished = {
            // isDragging = false
        },
        valueRange = valueRange,
        modifier = modifier,
        thumb = {
            Box(
                modifier = Modifier
                    .size(thumbSize)
                    .background(
                        brush = headerBrush,
                        shape = thumbShape
                    )
                    .padding(2.dp)
                    .background(
                        brush = headerBrush,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        },
        track = {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape)
                    .height(height = trackHeight)
            ) {
                // progress fraction between 0 and 1
                val fraction =
                    (progress - valueRange.start) / (valueRange.endInclusive - valueRange.start)
                val progressX = fraction * size.width
                val shape: Shape = CircleShape
                val color = Color(0x40000000)
                val blur: Dp = 4.dp
                val offsetY: Dp = 2.dp
                val offsetX: Dp = 2.dp
                val spread: Dp = 0.dp
                val trackWidth = size.width
                val trackTop = size.height / 2 - trackHeight.toPx() / 2

                // Unfilled part
                drawLine(
                    color = unfilledColor,
                    start = Offset(progressX, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = size.height
                )

                if (progressBarUnFill != null) {
                    drawRoundRect(
                        brush = progressBarUnFill,
                        topLeft = Offset(x = 0f, y = trackTop),
                        size = Size(width = trackWidth, height = trackHeight.toPx()),
                        cornerRadius = CornerRadius(
                            trackHeight.toPx() / 2,
                            trackHeight.toPx() / 2
                        ),
                        alpha = 0.8f
                    )
                } else {
                    drawIntoCanvas { canvas ->
                        val shadowSize =
                            Size(size.width + spread.toPx(), size.height + spread.toPx())
                        val shadowOutline =
                            shape.createOutline(shadowSize, layoutDirection, this)
                        val paint = Paint()
                        paint.color = color

                        canvas.saveLayer(size.toRect(), paint)
                        canvas.drawOutline(shadowOutline, paint)

//                        paint.asFrameworkPaint().apply {
//                            xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
//                            if (blur.toPx() > 0) {
//                                maskFilter =
//                                    BlurMaskFilter(blur.toPx(), BlurMaskFilter.Blur.NORMAL)
//                            }
//                        }
                        paint.color = Color.Black.copy(.7f)
                        canvas.translate(offsetX.toPx(), offsetY.toPx())
                        canvas.drawOutline(shadowOutline, paint)
                        canvas.restore()
                    }
                }

                // Filled part
                drawLine(
                    brush = fillBrush,
                    start = Offset(0f, size.height / 2),
                    end = Offset(progressX, size.height / 2),
                    strokeWidth = size.height
                )
            }
        }
    )
}

sealed class ProgressBarFill {
    data class Solid(val color: Color) : ProgressBarFill()
    data class Gradient(val brush: Brush) : ProgressBarFill()
}
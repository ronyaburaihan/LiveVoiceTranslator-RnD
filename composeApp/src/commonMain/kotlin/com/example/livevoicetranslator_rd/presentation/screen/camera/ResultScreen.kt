package com.example.livevoicetranslator_rd.presentation.screen.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.domain.model.OCRResult

@Composable
fun ResultScreen(
    imageBitmap: ImageBitmap?,
    ocrResult: OCRResult,
    onBack: () -> Unit,
    onTranslate: (String) -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
        },
        bottomBar = {
//            CameraActionBar(
//                onSpeakClick = { /* TODO */ },
//                onCopyClick = { /* TODO */ },
//                onSaveClick = { /* TODO */ },
//                onShareClick = { /* TODO */ }
//            )
        },
        containerColor = Color.Black
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            val screenWidth = maxWidth
            val screenHeight = maxHeight
            val density = LocalDensity.current

            imageBitmap?.let { bitmap ->
                // Calculate scale based on ContentScale.FillWidth
                val imageAspect = bitmap.height.toFloat() / bitmap.width.toFloat()
                val displayWidth = screenWidth.toPx(density)
                val displayHeight = displayWidth * imageAspect

                val scaleX = displayWidth / bitmap.width
                val scaleY = displayHeight / bitmap.height

                val offsetY = (screenHeight.toPx(density) - displayHeight) / 2f
                val offsetX = 0f // FillWidth centers horizontally by default

                // Display Image
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )

                // Overlay OCR Blocks
                ocrResult.blocks
                    .filter { it.confidence > 0.45f }
                    .forEach { block ->
                        val box = block.boundingBox
                        // Bounding boxes are normalized (0-1), so multiply by display dimensions
                        // Clamp values to ensure we don't exceed bounds
                        val clampedLeft = box.left.coerceIn(0f, 1f)
                        val clampedTop = box.top.coerceIn(0f, 1f)
                        val clampedRight = box.right.coerceIn(0f, 1f)
                        val clampedBottom = box.bottom.coerceIn(0f, 1f)

                        val left = clampedLeft * displayWidth + offsetX
                        val top = clampedTop * displayHeight + offsetY
                        val boxWidth = (clampedRight - clampedLeft) * displayWidth
                        val boxHeight = (clampedBottom - clampedTop) * displayHeight


                        val density = LocalDensity.current
                        val (fontSize, lineHeight) = autoFontSizeForBox(
                            text = block.text,
                            boxWidthPx = boxWidth,
                            boxHeightPx = boxHeight,
                            maxFontSize = 16f,
                            minFontSize = 6f,
                            lineHeightMultiplier = 1.15f // smoother for OCR
                        )

                        // Draw OCR block text at exact image coordinates
                        Text(
                            text = block.text,
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontSize = fontSize.sp,
                                lineHeight = lineHeight.sp
                            ),
                            modifier = Modifier
                                .offset(
                                    x = with(density) { left.toDp() },
                                    y = with(density) { top.toDp() }
                                )
                                .size(
                                    width = with(density) { boxWidth.toDp() },
                                    height = with(density) { boxHeight.toDp() }
                                )
                                .clip(RoundedCornerShape(2.dp))
                                .background(Color.Black.copy(alpha = 0.7f))
                                .padding(1.dp)
                        )
                    }
            }
        }
    }
}

@Composable
fun autoFontSizeForBox(
    text: String,
    boxWidthPx: Float,
    boxHeightPx: Float,
    maxFontSize: Float = 18f,
    minFontSize: Float = 6f,
    lineHeightMultiplier: Float = 1.2f
): Pair<Float, Float> {     // returns fontSize + lineHeight
    val textMeasurer = rememberTextMeasurer()

    var currentSize = maxFontSize

    while (currentSize >= minFontSize) {
        val style = TextStyle(
            fontSize = currentSize.sp,
            lineHeight = (currentSize * lineHeightMultiplier).sp
        )

        val result = textMeasurer.measure(
            text = text,
            style = style
        )

        if (result.size.width <= boxWidthPx && result.size.height <= boxHeightPx) {
            return currentSize to (currentSize * lineHeightMultiplier)
        }

        currentSize -= 1f
    }

    return minFontSize to (minFontSize * lineHeightMultiplier)
}

fun Dp.toPx(density: Density): Float = with(density) { this@toPx.toPx() }
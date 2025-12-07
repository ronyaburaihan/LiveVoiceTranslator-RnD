package com.example.livevoicetranslator_rd.presentation.screen.camera.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.core.platform.encodeBase64
import com.example.livevoicetranslator_rd.core.platform.toImageBitmap
import com.example.livevoicetranslator_rd.domain.model.TextBlock
import com.example.livevoicetranslator_rd.presentation.component.AppTopBar
import com.example.livevoicetranslator_rd.presentation.component.AppTopBarTitle
import com.example.livevoicetranslator_rd.presentation.component.LanguageDropdown
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import kotlinx.coroutines.launch
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_swap
import org.jetbrains.compose.resources.painterResource

@Composable
fun OCRResultScreen(
    viewModel: ResultViewModel,
    debugMode: Boolean = true
) {
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        if (uiState.imageBytes != null) {
            viewModel.extractTextFromImage(uiState.imageBytes?.encodeBase64())
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            AppTopBar(
                title = {
                    AppTopBarTitle(
                        title = "Result"
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        },
                        content = {
                            Icon(
                                modifier = Modifier.size(dimens.iconSize),
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Source Language
                        LanguageDropdown(
                            language = uiState.sourceLanguage.title,
                            onLanguageChange = { viewModel.updateSourceLanguage(it) }
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Swap Button
                        Image(
                            painter = painterResource(Res.drawable.ic_swap),
                            contentDescription = "Swap",
                            modifier = Modifier
                                .size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))

                        // Target Language
                        LanguageDropdown(
                            language = uiState.targetLanguage.title,
                            onLanguageChange = { viewModel.updateTargetLanguage(it) }
                        )

                        // Search Button
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            )
        },
        /*bottomBar = {
            CameraActionBar(
                onSpeakClick = { *//* TODO *//* },
                onCopyClick = { *//* TODO *//* },
                onSaveClick = { *//* TODO *//* },
                onShareClick = { *//* TODO *//* }
            )
        },*/
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

            val imageBitmap = uiState.imageBytes?.toImageBitmap()

            imageBitmap?.let { bitmap ->
                // Calculate scale based on ContentScale.FillWidth
                val imageAspect = bitmap.height.toFloat() / bitmap.width.toFloat()
                val displayWidth = screenWidth.toPx(density)
                val displayHeight = displayWidth * imageAspect

                val scaleX = displayWidth / bitmap.width
                val scaleY = displayHeight / bitmap.height

                val offsetY = (screenHeight.toPx(density) - displayHeight) / 2f
                val offsetX = 0f // FillWidth centers horizontally by default

                println("=== Image Display Info ===")
                println("Screen: ${screenWidth}x${screenHeight}")
                println("Image: ${bitmap.width}x${bitmap.height}")
                println("Display: ${displayWidth}x${displayHeight}")
                println("Offset: x=$offsetX, y=$offsetY")
                println("========================")

                // Display Image
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )

                if (uiState.isLoading){
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                scope.launch {
                    if (uiState.ocrResult?.detectedLanguage != null) {
                        viewModel.updateSourceLanguage(uiState.ocrResult!!.detectedLanguage!!)
                    } else {
                        uiState.ocrResult?.blocks?.first()?.text?.let {
                            viewModel.detectLanguage(it)
                        }
                    }
                }

                // Overlay OCR Blocks
                uiState.ocrResult?.blocks
                    ?.filter { it.confidence > 0.45f }
                    ?.forEach { block ->
                        val box = block.boundingBox


                        val density = LocalDensity.current

                        val translatedText by produceState(
                            initialValue = block.text,
                            key1 = block.text,
                            key2 = uiState.sourceLanguage,
                            key3 = uiState.targetLanguage
                        ) {
                            value = viewModel.translatePart(
                                block.text
                            )
                        }
                        if (debugMode) {
                            DebugTextBlock(
                                block = block,
                                displayWidth = displayWidth,
                                displayHeight = displayHeight,
                                offsetX = offsetX,
                                offsetY = offsetY,
                                density = density
                            )
                        } else {
                            RotatedTextBlock(
                                block = block,
                                displayWidth = displayWidth,
                                displayHeight = displayHeight,
                                offsetX = offsetX,
                                offsetY = offsetY,
                                density = density
                            )
                        }
                    }
            }
        }
    }
}

@Composable
fun DebugTextBlock(
    block: TextBlock,
    displayWidth: Float,
    displayHeight: Float,
    offsetX: Float,
    offsetY: Float,
    density: Density
) {
    val box = block.boundingBox

    val clampedLeft = box.left.coerceIn(0f, 1f)
    val clampedTop = box.top.coerceIn(0f, 1f)
    val clampedRight = box.right.coerceIn(0f, 1f)
    val clampedBottom = box.bottom.coerceIn(0f, 1f)

    val left = clampedLeft * displayWidth + offsetX
    val top = clampedTop * displayHeight + offsetY
    val boxWidth = (clampedRight - clampedLeft) * displayWidth
    val boxHeight = (clampedBottom - clampedTop) * displayHeight

    // Draw debug rectangle with rotation
    Box(
        modifier = Modifier
            .offset(
                x = with(density) { left.toDp() },
                y = with(density) { top.toDp() }
            )
            .size(
                width = with(density) { boxWidth.toDp() },
                height = with(density) { boxHeight.toDp() }
            )
            .graphicsLayer {
                rotationZ = block.rotationAngle
                transformOrigin = TransformOrigin.Center
            }
            .background(Color.Red.copy(alpha = 0.3f))
            .border(2.dp, Color.Yellow)
    ) {
        // Show confidence in corner
        Text(
            text = "${(block.confidence * 100).toInt()}%",
            color = Color.White,
            fontSize = 10.sp,
            modifier = Modifier
                .align(Alignment.TopStart)
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(2.dp)
        )
    }
}

@Composable
fun RotatedTextBlock(
    block: TextBlock,
    displayWidth: Float,
    displayHeight: Float,
    offsetX: Float,
    offsetY: Float,
    density: Density
) {
    val box = block.boundingBox

    val clampedLeft = box.left.coerceIn(0f, 1f)
    val clampedTop = box.top.coerceIn(0f, 1f)
    val clampedRight = box.right.coerceIn(0f, 1f)
    val clampedBottom = box.bottom.coerceIn(0f, 1f)

    val left = clampedLeft * displayWidth + offsetX
    val top = clampedTop * displayHeight + offsetY
    val boxWidth = (clampedRight - clampedLeft) * displayWidth
    val boxHeight = (clampedBottom - clampedTop) * displayHeight

    val (fontSize, lineHeight) = autoFontSizeForBox(
        text = block.text,
        boxWidthPx = boxWidth,
        boxHeightPx = boxHeight,
        maxFontSize = 16f,
        minFontSize = 6f,
        lineHeightMultiplier = 1.15f
    )

    Box(
        modifier = Modifier
            .offset(
                x = with(density) { left.toDp() },
                y = with(density) { top.toDp() }
            )
            .size(
                width = with(density) { boxWidth.toDp() },
                height = with(density) { boxHeight.toDp() }
            )
            .graphicsLayer {
                rotationZ = block.rotationAngle
                transformOrigin = TransformOrigin.Center
            }
    ) {
        Text(
            text = block.text,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = fontSize.sp,
                lineHeight = lineHeight.sp
            ),
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(2.dp))
                .background(Color.Black.copy(alpha = 0.7f))
                .padding(1.dp)
        )
    }
}

@Composable
fun autoFontSizeForBox(
    text: String,
    boxWidthPx: Float,
    boxHeightPx: Float,
    maxFontSize: Float = 50f,
    minFontSize: Float = 8f,
    lineHeightMultiplier: Float = 1.2f
): Pair<Float, Float> {
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
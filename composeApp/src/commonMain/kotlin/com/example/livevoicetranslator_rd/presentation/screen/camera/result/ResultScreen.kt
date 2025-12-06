package com.example.livevoicetranslator_rd.presentation.screen.camera.result

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.core.platform.OCRProcessor
import com.example.livevoicetranslator_rd.core.platform.decodeBase64
import com.example.livevoicetranslator_rd.core.platform.toImageBitmap
import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.ImageSource
import com.example.livevoicetranslator_rd.domain.model.OCRResult
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
    viewModel: ResultViewModel
) {
    val navController = LocalNavController.current
    val scope = rememberCoroutineScope()
    var ocrResult by remember { mutableStateOf<OCRResult?>(null) }
    val uiState by viewModel.uiState.collectAsState()

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

                val cameraImage = CameraImage(
                    imageData = uiState.imageBytes!!,
                    width = 0, // Unknown
                    height = 0, // Unknown
                    source = ImageSource.GALLERY
                )

                // Display Image
                Image(
                    bitmap = bitmap,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                )

                scope.launch {
                    val ocrResponse = OCRProcessor().recognizeText(cameraImage, "en")
                    ocrResponse.onSuccess {
                        println("OCR Result: $it")
                        ocrResult = it
                    }.onFailure {
                        println("OCR Error: $it")
                        ocrResult = null
                    }
                }

                scope.launch {
                    if (ocrResult?.detectedLanguage != null) {
                        viewModel.updateSourceLanguage(ocrResult!!.detectedLanguage!!)
                    } else {
                        ocrResult?.blocks?.first()?.text?.let {
                            viewModel.detectLanguage(it)
                        }
                    }
                }

                // Overlay OCR Blocks
                ocrResult?.blocks
                    ?.filter { it.confidence > 0.45f }
                    ?.forEach { block ->
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
                        // Draw OCR block text at exact image coordinates
                        Text(
                            text = translatedText,
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
                                .sizeIn(
                                    minWidth = with(density) { boxWidth.toDp() },
                                    minHeight = with(density) { boxHeight.toDp() }
                                )
                                .wrapContentSize()
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
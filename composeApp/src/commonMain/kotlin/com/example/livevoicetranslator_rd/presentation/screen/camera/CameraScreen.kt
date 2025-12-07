package com.example.livevoicetranslator_rd.presentation.screen.camera

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.core.platform.CameraController
import com.example.livevoicetranslator_rd.core.platform.CameraPreview
import com.example.livevoicetranslator_rd.core.platform.FlashMode
import com.example.livevoicetranslator_rd.core.platform.Permission
import com.example.livevoicetranslator_rd.core.platform.encodeBase64
import com.example.livevoicetranslator_rd.core.platform.rememberCameraController
import com.example.livevoicetranslator_rd.core.platform.rememberPermissionState
import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.screen.camera.result.ResultViewModel
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryBrush
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_upload
import org.jetbrains.compose.resources.painterResource

@Composable
fun CameraScreen(
    viewModel: ResultViewModel
) {
    val cameraPermissionState = rememberPermissionState(Permission.CAMERA)
    var imageByte by remember { mutableStateOf<ByteArray?>(null) }
    val navController = LocalNavController.current

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    if (cameraPermissionState.isGranted) {
        val cameraController = rememberCameraController()
        val scope = rememberCoroutineScope()
        var capturedOcrResult by remember { mutableStateOf<OCRResult?>(null) }
        var isLiveMode by remember { mutableStateOf(false) }
        var detectedText by remember { mutableStateOf("") }

        val imagePicker =
            remember { com.example.livevoicetranslator_rd.core.platform.ImagePicker() }
        imagePicker.RegisterPicker { imageBytes ->
            viewModel.updateImageBytes(imageBytes)
            navController.navigate(ScreenRoute.OCRResultScreen)
        }

        LaunchedEffect(cameraController) {
            cameraController.initialize()
            cameraController.startPreview()
        }

        DisposableEffect(cameraController) {
            onDispose {
                // Stop preview when composable is disposed
                // Use coroutine scope to call suspend function
                scope.launch { cameraController.stopPreview() }
            }
        }

        CameraScreenContent(
            cameraController = cameraController,
            isLiveMode = isLiveMode,
            capturedOcrResult = capturedOcrResult,
            onToggleLiveMode = {
                isLiveMode = !isLiveMode
                scope.launch {
                    if (isLiveMode) {
                        cameraController.startLiveMode { text ->
                            detectedText = text
                        }
                    } else {
                        cameraController.stopLiveMode()
                        detectedText = ""
                    }
                }
            },
            onPickImage = {
                imagePicker.pickImage()
            },
            scope = scope,
            onCapture = {
                scope.launch {
                    val result = cameraController.capturePhoto()
                    result.onSuccess { image ->
                        imageByte = image.imageData
                        viewModel.updateImageBytes(image.imageData)
                        navController.navigate(ScreenRoute.OCRResultScreen)
                        // Perform OCR on captured image
                        /*val ocrResult = OCRProcessor().recognizeText(image, "hi")
                        ocrResult.onSuccess {
                            capturedOcrResult = it
                        }.onFailure {
                            capturedOcrResult = null
                        }*/
                    }
                }
            }
        )
    } else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Camera permission required")
            Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
                Text("Grant Permission")
            }
        }
    }
}

@Composable
private fun CameraScreenContent(
    cameraController: CameraController,
    isLiveMode: Boolean,
    capturedOcrResult: OCRResult?,
    scope: CoroutineScope,
    onPickImage: () -> Unit,
    onToggleLiveMode: () -> Unit,
    onCapture: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            CameraPreview(
                controller = cameraController,
                modifier = Modifier.fillMaxSize()
            )

            // Overlay for detected text
            if (isLiveMode && capturedOcrResult?.fullText?.isNotEmpty() == true) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .background(Color.Black.copy(alpha = 0.5f))
                        .padding(16.dp)
                ) {
                    Text(
                        text = capturedOcrResult.fullText,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Controls
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(24.dp)
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val flashMode by cameraController.flashState.collectAsState()
                IconButton(
                    onClick = {
                        scope.launch { cameraController.toggleFlash() }
                    }
                ) {
                    Icon(
                        imageVector = when (flashMode) {
                            FlashMode.ON -> Icons.Default.FlashOn
                            FlashMode.AUTO -> Icons.Default.FlashOn // Use same icon or different
                            FlashMode.OFF -> Icons.Default.FlashOff
                        },
                        contentDescription = "Flash",
                        tint = if (flashMode == FlashMode.OFF) Color.White else Color.Yellow
                    )
                }

                IconButton(onClick = { onPickImage() }) {
                    Icon(Icons.Default.Image, contentDescription = "Gallery", tint = Color.White)
                }

                IconButton(
                    onClick = onCapture,
                    modifier = Modifier
                        .height(64.dp)
                        .width(64.dp)
                        .background(Color.White, androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(Icons.Default.Camera, contentDescription = "Capture", tint = Color.Black)
                }

                IconButton(onClick = onToggleLiveMode) {
                    Icon(
                        imageVector = if (isLiveMode) Icons.Default.TextFields else Icons.Default.Camera,
                        contentDescription = "Toggle Live Mode",
                        tint = if (isLiveMode) MaterialTheme.colorScheme.primary else Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun ImagePickerLayout(
    modifier: Modifier = Modifier,
    title: String,
    imageBitmap: ImageBitmap? = null,
    onPickImage: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = title,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            imageBitmap?.let {
                Image(
                    modifier = Modifier.height(100.dp)
                        .width(150.dp)
                        .clip(RoundedCornerShape(dimens.cornerRadiusSmall)),
                    bitmap = imageBitmap,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
                Spacer(modifier = Modifier.width(dimens.spaceBetween))
            }
            Column(
                modifier = Modifier.weight(1f)
                    .heightIn(min = 100.dp)
                    .clip(RoundedCornerShape(dimens.cornerRadiusSmall))
                    .background(MaterialTheme.colorScheme.surface)
                    .clickable { onPickImage.invoke() }
                    .drawBehind {
                        val stroke = Stroke(
                            width = 3.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
                        )
                        drawRoundRect(
                            brush = PrimaryBrush,
                            style = stroke,
                            cornerRadius = CornerRadius(8.dp.toPx())
                        )
                    }.padding(dimens.contentPadding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_upload),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "Click here to select a photo",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

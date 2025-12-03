package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.model.CameraImage
import kotlinx.coroutines.flow.StateFlow
import android.content.Context
import androidx.lifecycle.LifecycleOwner
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.CameraSelector
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

actual class CameraController(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner
) {
    private var cameraProvider: ProcessCameraProvider? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalysis: ImageAnalysis? = null
    private val executor = Executors.newSingleThreadExecutor()
    private var previewView: androidx.camera.view.PreviewView? = null

    fun setPreviewView(view: androidx.camera.view.PreviewView) {
        previewView = view
    }

    actual val previewState: StateFlow<CameraPreviewState>
        get() = TODO("Not yet implemented")

    actual suspend fun initialize() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProvider = suspendCoroutine { continuation ->
            cameraProviderFuture.addListener({
                continuation.resume(cameraProviderFuture.get())
            }, ContextCompat.getMainExecutor(context))
        }
    }

    actual suspend fun startPreview() {
        val cameraProvider = cameraProvider ?: return
        val preview = Preview.Builder().build()

        previewView?.let { view ->
            preview.surfaceProvider = view.surfaceProvider
        }

        imageCapture = ImageCapture.Builder()
            .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
            .build()

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (exc: Exception) {
            Log.e("CameraController", "Use case binding failed", exc)
        }
    }

    actual suspend fun stopPreview() {
        cameraProvider?.unbindAll()
    }

    actual suspend fun capturePhoto(): Result<CameraImage> {
        val imageCapture =
            imageCapture ?: return Result.failure(Exception("Camera not initialized"))

        return suspendCoroutine { continuation ->
            imageCapture.takePicture(
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        // Convert ImageProxy to CameraImage
                        // For now, we just return a dummy or implement conversion
                        // Since CameraImage uses ByteArray, we need to convert.
                        // This is expensive.

                        val buffer = image.planes[0].buffer
                        val bytes = ByteArray(buffer.remaining())
                        buffer.get(bytes)

                        val cameraImage = CameraImage(
                            imageData = bytes,
                            width = image.width,
                            height = image.height,
                            rotation = image.imageInfo.rotationDegrees,
                            source = com.example.livevoicetranslator_rd.domain.model.ImageSource.CAMERA
                        )

                        image.close()
                        continuation.resume(Result.success(cameraImage))
                    }

                    override fun onError(exception: ImageCaptureException) {
                        continuation.resume(Result.failure(exception))
                    }
                }
            )
        }
    }

    private val _flashState = kotlinx.coroutines.flow.MutableStateFlow(FlashMode.OFF)
    actual val flashState: StateFlow<FlashMode> = _flashState

    actual suspend fun toggleFlash() {
        val imageCapture = imageCapture ?: return
        val currentMode = _flashState.value
        val newMode = when (currentMode) {
            FlashMode.OFF -> FlashMode.ON
            FlashMode.ON -> FlashMode.AUTO
            FlashMode.AUTO -> FlashMode.OFF
        }

        val flashMode = when (newMode) {
            FlashMode.ON -> ImageCapture.FLASH_MODE_ON
            FlashMode.AUTO -> ImageCapture.FLASH_MODE_AUTO
            FlashMode.OFF -> ImageCapture.FLASH_MODE_OFF
        }

        imageCapture.flashMode = flashMode
        _flashState.value = newMode

        // Also update camera control for torch if needed, but ImageCapture handles flash for photo.
        // For preview/live mode, we might want torch.
        // Let's stick to ImageCapture flash for now.
    }

    actual suspend fun release() {
        executor.shutdown()
    }

    @OptIn(ExperimentalGetImage::class)
    actual suspend fun startLiveMode(onTextDetected: (String) -> Unit) {
        val cameraProvider = cameraProvider ?: return

        imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis?.setAnalyzer(executor) { imageProxy ->
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                // We need an instance of OCRProcessor. 
                // Since we can't easily inject it here without changing constructor, 
                // we'll instantiate it or use a singleton if available.
                // For now, let's just use the helper method we added to OCRProcessor class if it was static, 
                // but it's an instance method.
                // We can create a new instance of OCRProcessor() since it has no state?
                // Wait, OCRProcessor might need context or model download management.
                // Let's create a local instance for now.
                val ocrProcessor = OCRProcessor()

                // We need to call the suspend function recognizeText.
                // Since we are in a background thread (executor), we can runBlocking or use a scope.
                // But setAnalyzer is not suspend.

                // Better: Use ML Kit directly here to avoid complexity of calling suspend function from analyzer callback
                val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        onTextDetected(visionText.text)
                        imageProxy.close()
                    }
                    .addOnFailureListener { e ->
                        Log.e("CameraController", "Text recognition failed", e)
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        }

        // Re-bind to include analysis
        val preview = Preview.Builder().build()
        previewView?.let { view ->
            preview.surfaceProvider = view.surfaceProvider
        }

        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                cameraSelector,
                preview,
                imageCapture, // Keep image capture
                imageAnalysis
            )
        } catch (exc: Exception) {
            Log.e("CameraController", "Use case binding failed", exc)
        }
    }

    actual suspend fun stopLiveMode() {
        // Unbind analysis or just stop it
        startPreview() // Re-bind without analysis
    }
}
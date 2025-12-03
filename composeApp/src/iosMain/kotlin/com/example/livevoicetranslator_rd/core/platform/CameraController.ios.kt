package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.ImageSource
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import platform.AVFoundation.AVCaptureConnection
import platform.AVFoundation.AVCaptureDevice
import platform.AVFoundation.AVCaptureDeviceDiscoverySession
import platform.AVFoundation.AVCaptureDeviceInput
import platform.AVFoundation.AVCaptureDevicePositionBack
import platform.AVFoundation.AVCaptureDeviceTypeBuiltInWideAngleCamera
import platform.AVFoundation.AVCaptureFlashModeAuto
import platform.AVFoundation.AVCaptureFlashModeOff
import platform.AVFoundation.AVCaptureFlashModeOn
import platform.AVFoundation.AVCaptureOutput
import platform.AVFoundation.AVCapturePhoto
import platform.AVFoundation.AVCapturePhotoCaptureDelegateProtocol
import platform.AVFoundation.AVCapturePhotoOutput
import platform.AVFoundation.AVCapturePhotoSettings
import platform.AVFoundation.AVCaptureSession
import platform.AVFoundation.AVCaptureSessionPresetPhoto
import platform.AVFoundation.AVCaptureVideoDataOutput
import platform.AVFoundation.AVCaptureVideoDataOutputSampleBufferDelegateProtocol
import platform.AVFoundation.AVMediaTypeVideo
import platform.AVFoundation.fileDataRepresentation
import platform.CoreMedia.CMSampleBufferRef
import platform.Foundation.NSError
import platform.darwin.NSObject
import platform.darwin.dispatch_get_main_queue
import platform.darwin.dispatch_queue_create
import kotlin.coroutines.resume

// -------------------------
// NSData extension
// -------------------------
import kotlinx.cinterop.*
import platform.Foundation.*

@OptIn(ExperimentalForeignApi::class)
actual class CameraController {
    private val _previewState = MutableStateFlow<CameraPreviewState>(CameraPreviewState.Idle)
    actual val previewState: StateFlow<CameraPreviewState> = _previewState

    private val _flashState = MutableStateFlow(FlashMode.OFF)
    actual val flashState: StateFlow<FlashMode> = _flashState

    val session = AVCaptureSession()
    private var device: AVCaptureDevice? = null
    private var input: AVCaptureDeviceInput? = null
    private var photoOutput = AVCapturePhotoOutput()
    private var videoOutput = AVCaptureVideoDataOutput()

    private val cameraQueue = dispatch_queue_create("cameraQueue", null)
    private var isLiveMode = false
    private var onTextDetectedCallback: ((String) -> Unit)? = null

    // -------------------------
    // Photo delegate
    // -------------------------
    private val photoDelegate = object : NSObject(), AVCapturePhotoCaptureDelegateProtocol {
        private var continuation: kotlin.coroutines.Continuation<Result<CameraImage>>? = null

        fun setContinuation(cont: kotlin.coroutines.Continuation<Result<CameraImage>>) {
            continuation = cont
        }

        override fun captureOutput(
            output: AVCapturePhotoOutput,
            didFinishProcessingPhoto: AVCapturePhoto,
            error: NSError?
        ) {
            if (error != null) {
                dispatch_async(dispatch_get_main_queue()) {
                    continuation?.resume(Result.failure(Exception(error.localizedDescription)))
                    continuation = null
                }
                return
            }

            val fileData = didFinishProcessingPhoto.fileDataRepresentation()
            if (fileData == null) {
                dispatch_async(dispatch_get_main_queue()) {
                    continuation?.resume(Result.failure(Exception("No photo data")))
                    continuation = null
                }
                return
            }

            val bytes = fileData.toByteArrayy()

            val image = CameraImage(
                imageData = bytes,
                width = 0, // Can be filled later if needed
                height = 0,
                source = ImageSource.CAMERA
            )

            dispatch_async(dispatch_get_main_queue()) {
                continuation?.resume(Result.success(image))
                continuation = null
            }
        }
    }

    // -------------------------
    // Video delegate (live mode stub)
    // -------------------------
    private val videoDelegate = object : NSObject(), AVCaptureVideoDataOutputSampleBufferDelegateProtocol {
        override fun captureOutput(
            output: AVCaptureOutput,
            didOutputSampleBuffer: CMSampleBufferRef?,
            fromConnection: AVCaptureConnection
        ) {
            if (!isLiveMode || didOutputSampleBuffer == null) return

            // TODO: Implement OCR using Vision on CVPixelBuffer
            // This is a stub for live mode
        }
    }

    // -------------------------
    // Initialize camera
    // -------------------------
    actual suspend fun initialize() {
        session.beginConfiguration()
        session.sessionPreset = AVCaptureSessionPresetPhoto

        val discoverySession = AVCaptureDeviceDiscoverySession.discoverySessionWithDeviceTypes(
            listOf(AVCaptureDeviceTypeBuiltInWideAngleCamera),
            AVMediaTypeVideo,
            AVCaptureDevicePositionBack
        )

        device = discoverySession.devices.firstOrNull() as? AVCaptureDevice

        device?.let { dev ->
            try {
                val inputDevice = AVCaptureDeviceInput.deviceInputWithDevice(dev, null)
                if (inputDevice != null && session.canAddInput(inputDevice)) {
                    session.addInput(inputDevice)
                    this.input = inputDevice
                }

                if (session.canAddOutput(photoOutput)) session.addOutput(photoOutput)
                if (session.canAddOutput(videoOutput)) {
                    videoOutput.setSampleBufferDelegate(videoDelegate, cameraQueue)
                    session.addOutput(videoOutput)
                }

                session.commitConfiguration()
                _previewState.value = CameraPreviewState.Ready
            } catch (e: Exception) {
                println("Error initializing camera: ${e.message}")
                session.commitConfiguration() // Ensure commit is called even on error
            }
        }
    }

    // -------------------------
    // Capture photo
    // -------------------------
    actual suspend fun capturePhoto(): Result<CameraImage> = suspendCancellableCoroutine { cont ->
        val settings = AVCapturePhotoSettings.photoSettings()

        val flashMode = when (_flashState.value) {
            FlashMode.ON -> AVCaptureFlashModeOn
            FlashMode.OFF -> AVCaptureFlashModeOff
            FlashMode.AUTO -> AVCaptureFlashModeAuto
        }
        if (photoOutput.supportedFlashModes.contains(flashMode)) {
            settings.flashMode = flashMode
        }

        photoDelegate.setContinuation(cont)
        photoOutput.capturePhotoWithSettings(settings, photoDelegate)
    }

    // -------------------------
    // Toggle flash
    // -------------------------
    actual suspend fun toggleFlash() {
        val newMode = when (_flashState.value) {
            FlashMode.OFF -> FlashMode.ON
            FlashMode.ON -> FlashMode.AUTO
            FlashMode.AUTO -> FlashMode.OFF
        }
        _flashState.value = newMode
    }

    // -------------------------
    // Release resources
    // -------------------------
    actual suspend fun release() {
        if (session.isRunning()) {
            session.stopRunning()
        }
    }

    // -------------------------
    // Live mode
    // -------------------------
    actual suspend fun startLiveMode(onTextDetected: (String) -> Unit) {
        isLiveMode = true
        onTextDetectedCallback = onTextDetected
    }

    actual suspend fun stopLiveMode() {
        isLiveMode = false
        onTextDetectedCallback = null
    }

    actual suspend fun startPreview() {
        if (!session.isRunning()) {
            session.startRunning()
        }
    }

    actual suspend fun stopPreview() {
        if (session.isRunning()) {
            session.stopRunning()
        }
    }
}

// -------------------------
// Dispatch helper
// -------------------------
fun dispatch_async(queue: platform.darwin.dispatch_queue_t, block: () -> Unit) {
    platform.darwin.dispatch_async(queue) {
        block()
    }
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArrayy(): ByteArray {
    val length = this.length.toInt()
    val byteArray = ByteArray(length)
    if (length == 0) return byteArray

    // Use memScoped and reinterpret<ByteVar>() to read the bytes
    this.bytes?.reinterpret<ByteVar>()?.let { ptr ->
        for (i in 0 until length) {
            byteArray[i] = ptr[i]
        }
    }
    return byteArray
}

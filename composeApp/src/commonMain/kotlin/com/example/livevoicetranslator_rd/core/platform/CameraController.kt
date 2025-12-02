package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.model.CameraImage
import kotlinx.coroutines.flow.StateFlow

expect class CameraController {
    val previewState: StateFlow<CameraPreviewState>
    val flashState: StateFlow<FlashMode>

    suspend fun initialize()
    suspend fun startPreview()
    suspend fun stopPreview()
    suspend fun capturePhoto(): Result<CameraImage>
    suspend fun toggleFlash()
    suspend fun release()
    
    suspend fun startLiveMode(onTextDetected: (String) -> Unit)
    suspend fun stopLiveMode()
}

sealed class CameraPreviewState {
    object Idle : CameraPreviewState()
    object Ready : CameraPreviewState()
    data class Error(val message: String) : CameraPreviewState()
}

enum class FlashMode {
    OFF, ON, AUTO
}
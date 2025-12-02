package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.OCRResult

sealed class CameraTranslationState {
    data class Loading(val message: String) : CameraTranslationState()
    data class ImageCaptured(val image: CameraImage) : CameraTranslationState()
    data class ImageProcessed(val image: CameraImage) : CameraTranslationState()
    data class TextRecognized(val ocrResult: OCRResult) : CameraTranslationState()
    data class Complete(
        val image: CameraImage,
        val ocrResult: OCRResult,
        val translation: String
    ) : CameraTranslationState()
    data class Error(val message: String) : CameraTranslationState()
    data class LimitReached(val current: Int, val max: Int) : CameraTranslationState()
}
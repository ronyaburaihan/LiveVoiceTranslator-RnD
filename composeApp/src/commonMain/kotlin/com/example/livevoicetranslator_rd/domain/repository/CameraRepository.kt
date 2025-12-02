package com.example.livevoicetranslator_rd.domain.repository

import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.CameraTranslation
import com.example.livevoicetranslator_rd.domain.model.OCRResult

interface CameraRepository {
    suspend fun captureImage(): Result<CameraImage>
    suspend fun pickImageFromGallery(): Result<CameraImage>
    suspend fun recognizeText(
        image: CameraImage,
        languageHints: List<String> = emptyList()
    ): Result<OCRResult>
    suspend fun saveToHistory(translation: CameraTranslation): Result<Unit>
    suspend fun exportImage(image: CameraImage, text: String): Result<String>
}
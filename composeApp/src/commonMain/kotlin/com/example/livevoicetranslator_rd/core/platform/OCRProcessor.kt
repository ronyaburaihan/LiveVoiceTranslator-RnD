package com.example.livevoicetranslator_rd.core.platform

import com.example.livevoicetranslator_rd.domain.model.CameraImage
import com.example.livevoicetranslator_rd.domain.model.OCRResult

expect class OCRProcessor() {
    suspend fun recognizeText(
        image: CameraImage,
        languageHints: List<String>
    ): Result<OCRResult>

    suspend fun downloadModel(languageCode: String): Result<Unit>
    suspend fun isModelDownloaded(languageCode: String): Boolean
}
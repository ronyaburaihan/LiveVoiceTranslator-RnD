package com.example.livevoicetranslator_rd.domain.usecase.ocr

import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.domain.repository.ImageProcessingRepository

class ExtractTextFromImage(
    private val imageProcessingRepository: ImageProcessingRepository
) {
    suspend operator fun invoke(imageBase64: String): Result<OCRResult> {
        return imageProcessingRepository.extractTextFromImage(imageBase64)
    }
}
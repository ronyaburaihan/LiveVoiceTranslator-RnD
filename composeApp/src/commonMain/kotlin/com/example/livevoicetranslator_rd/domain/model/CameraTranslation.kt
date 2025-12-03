package com.example.livevoicetranslator_rd.domain.model

data class CameraTranslation(
    val id: String,
    val imageData: ByteArray,
    val detectedText: String,
    val translatedText: String,
    val sourceLang: String,
    val targetLang: String,
    val ocrEngine: OCREngine,
    val isFavorite: Boolean = false
)
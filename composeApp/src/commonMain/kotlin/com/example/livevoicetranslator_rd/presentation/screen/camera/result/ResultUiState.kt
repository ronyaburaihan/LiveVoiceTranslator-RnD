package com.example.livevoicetranslator_rd.presentation.screen.camera.result

import com.example.livevoicetranslator_rd.domain.model.OCRResult
import com.example.livevoicetranslator_rd.domain.model.TranslatableLanguages

data class ResultUiState(
    val sourceLanguage: TranslatableLanguages = TranslatableLanguages.English,
    val targetLanguage: TranslatableLanguages = TranslatableLanguages.Bengali,
    val imageBytes: ByteArray? = null,
    val ocrResult: OCRResult? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

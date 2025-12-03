package com.example.livevoicetranslator_rd.presentation.screen.translate

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.ModelDownloadState
import com.example.livevoicetranslator_rd.domain.model.TranslationResult


data class TranslateUiState(
    val inputText: String = "",
    val targetLang: String = "en",
    val detectedLanguage: String? = null,
    val translatedText: String? = null,
    val history: List<TranslationResult> = emptyList(),
    val charCount: Int = 0,
    val isLoading: Boolean = false,
    val languageDetectionResult: LanguageDetectionResult = LanguageDetectionResult(
        languageCode = "",
        confidence = 0.0f
    ),
    val modelDownloadState: ModelDownloadState = ModelDownloadState.Idle,
    val error: String? = null
)

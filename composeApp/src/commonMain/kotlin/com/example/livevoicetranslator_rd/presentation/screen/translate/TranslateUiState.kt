package com.example.livevoicetranslator_rd.presentation.screen.translate

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.ModelDownloadState
import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.model.TranslationEngine


data class TranslateUiState(
    val inputText: String = "",
    val sourceLang: String = "en",
    val targetLang: String = "es",
    val detectedLanguage: String? = null,
    val translatedText: String? = null,
    val detectedEngine: TranslationEngine? = null,
    val history: List<OrchestratorResult> = emptyList(),
    val charCount: Int = 0,
    val isLoading: Boolean = false,
    val languageDetectionResult: LanguageDetectionResult = LanguageDetectionResult(
        languageCode = "",
        confidence = 0.0f
    ),
    val modelDownloadState: ModelDownloadState = ModelDownloadState.Idle,
    val error: String? = null
)

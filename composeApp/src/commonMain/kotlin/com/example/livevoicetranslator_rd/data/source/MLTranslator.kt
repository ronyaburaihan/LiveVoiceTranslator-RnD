package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult

interface MLTranslator {
    suspend fun detectLanguage(text: String): LanguageDetectionResult
    suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String

    suspend fun downloadModelIfNeeded(sourceLang: String, targetLang: String): Boolean
}

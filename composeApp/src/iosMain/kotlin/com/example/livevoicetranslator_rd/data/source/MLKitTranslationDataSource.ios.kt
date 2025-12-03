package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult

actual class MLKitTranslationDataSource actual constructor() :
    MLTranslator {
    actual override suspend fun detectLanguage(text: String): LanguageDetectionResult {
        return LanguageDetectionResult(
            languageCode = "en",
            confidence = 1.0f
        )
    }

    actual override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String {
        return ""
    }

    actual override suspend fun downloadModelIfNeeded(
        sourceLang: String,
        targetLang: String
    ): Boolean {
        TODO("Not yet implemented")
    }
}
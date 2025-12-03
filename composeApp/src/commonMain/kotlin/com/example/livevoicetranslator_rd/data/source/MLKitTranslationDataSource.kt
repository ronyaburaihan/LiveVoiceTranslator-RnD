package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult

expect class MLKitTranslationDataSource() : MLTranslator {
    override suspend fun detectLanguage(text: String): LanguageDetectionResult
    override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String
    override suspend fun downloadModelIfNeeded(sourceLang: String, targetLang: String): Boolean
}
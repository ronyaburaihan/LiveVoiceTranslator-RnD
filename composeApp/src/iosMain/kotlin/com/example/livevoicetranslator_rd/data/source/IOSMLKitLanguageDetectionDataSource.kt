package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult

class IOSMLKitLanguageDetectionDataSource : MLKitLanguageDetectionDataSource {
    override suspend fun detectLanguage(text: String): LanguageDetectionResult {
        TODO("Not yet implemented")

    }
}
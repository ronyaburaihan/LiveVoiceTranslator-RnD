package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult

interface MLKitLanguageDetectionDataSource {
    suspend fun detectLanguage(text: String): LanguageDetectionResult
}

package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult

interface TranslationOrchestratorDataSource {
    suspend fun detectLanguage(text: String): LanguageDetectionResult
    suspend fun translate(text: String, source: String?, target: String): OrchestratorResult
}
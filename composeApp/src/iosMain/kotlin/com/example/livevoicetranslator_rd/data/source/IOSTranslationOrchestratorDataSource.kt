package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult

class IOSTranslationOrchestratorDataSource: TranslationOrchestratorDataSource {
    override suspend fun detectLanguage(text: String): LanguageDetectionResult {
        TODO("Not yet implemented")
    }

    override suspend fun translate(
        text: String,
        source: String?,
        target: String
    ): OrchestratorResult {
        TODO("Not yet implemented")
    }
}
package com.example.livevoicetranslator_rd.data.repository

import com.example.livevoicetranslator_rd.data.source.HistoryDataSource
import com.example.livevoicetranslator_rd.data.source.LocalCacheDataSource
import com.example.livevoicetranslator_rd.data.source.TranslationOrchestratorDataSource
import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class TranslationRepositoryImpl(
    private val orchestrator: TranslationOrchestratorDataSource,
    private val cache: LocalCacheDataSource,
    private val history: HistoryDataSource
) : TranslationRepository {

    override suspend fun translate(request: TranslationRequest): OrchestratorResult {

        // 1. Check cache first
        cache.get(request)?.let { return it }

        // 2. Use orchestrator to handle translation fallback
        val result = orchestrator.translate(request.text, request.sourceLang, request.targetLang)

        // 3. Save to cache & history
        cache.put(request, result)
        history.add(result)

        return result
    }

    override suspend fun detectLanguage(text: String): LanguageDetectionResult {
        return orchestrator.detectLanguage(text)
    }

    override suspend fun saveFavorite(result: OrchestratorResult) {
        history.add(result) // simple: add to favorites
    }

    override suspend fun getHistory(): List<OrchestratorResult> {
        return history.getAll()
    }

    override suspend fun clearHistory() {
        history.clear()
    }

    override suspend fun getCached(request: TranslationRequest): OrchestratorResult? {
        return cache.get(request)
    }
}
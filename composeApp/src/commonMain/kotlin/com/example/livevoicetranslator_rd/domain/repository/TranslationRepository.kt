package com.example.livevoicetranslator_rd.domain.repository

import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest


interface TranslationRepository {

    suspend fun translate(request: TranslationRequest): OrchestratorResult

    suspend fun detectLanguage(text: String): LanguageDetectionResult

    suspend fun saveFavorite(result: OrchestratorResult)

    suspend fun getHistory(): List<OrchestratorResult>

    suspend fun clearHistory()

    suspend fun getCached(request: TranslationRequest): OrchestratorResult?
}
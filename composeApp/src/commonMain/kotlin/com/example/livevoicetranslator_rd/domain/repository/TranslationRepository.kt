package com.example.livevoicetranslator_rd.domain.repository

import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.model.TranslationResult


interface TranslationRepository {

    suspend fun translate(request: TranslationRequest): TranslationResult

    suspend fun detectLanguage(text: String): String

    suspend fun saveFavorite(result: TranslationResult)

    suspend fun getHistory(): List<TranslationResult>

    suspend fun clearHistory()

    suspend fun getCached(request: TranslationRequest): TranslationResult?
}
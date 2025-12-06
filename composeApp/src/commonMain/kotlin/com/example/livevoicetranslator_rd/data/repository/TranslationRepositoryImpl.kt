package com.example.livevoicetranslator_rd.data.repository

import com.example.livevoicetranslator_rd.data.mapper.mapToResult
import com.example.livevoicetranslator_rd.data.source.CloudTranslationDataSource
import com.example.livevoicetranslator_rd.data.source.HistoryDataSource
import com.example.livevoicetranslator_rd.data.source.LocalCacheDataSource
import com.example.livevoicetranslator_rd.data.source.MLTranslator
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.model.TranslationResult
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class TranslationRepositoryImpl(
    private val mlKit: MLTranslator,
    private val cloud: CloudTranslationDataSource,
    private val cache: LocalCacheDataSource,
    private val history: HistoryDataSource
) : TranslationRepository {

    override suspend fun translate(request: TranslationRequest): TranslationResult {

        // 1. Check cache
        cache.get(request)?.let { return it }

        // 2. Offline attempt using MLKit
        val source = request.sourceLang ?: mlKit.detectLanguage(request.text)

        return try {
            val translated = mlKit.translate(
                text = request.text,
                sourceLang = source,
                targetLang = request.targetLang
            )

            val result = mapToResult(
                input = request.text,
                output = translated,
                source = source,
                target = request.targetLang
            )

            // save to cache & history
            cache.put(request, result)
            history.add(result)

            result

        } catch (offlineError: Exception) {

            println("Offline translation failed: $offlineError")


            // 3. Cloud fallback
            val translatedCloud = cloud.translateOnline(
                text = request.text,
                sourceLang = source,
                targetLang = request.targetLang
            )

            val result = mapToResult(
                input = request.text,
                output = translatedCloud,
                source = source,
                target = request.targetLang
            )

            cache.put(request, result)
            history.add(result)

            result
        }
    }

    override suspend fun detectLanguage(text: String): String {
        return mlKit.detectLanguage(text)
    }

    override suspend fun saveFavorite(result: TranslationResult) {
        history.add(result) // simple: add to favorites
    }

    override suspend fun getHistory(): List<TranslationResult> {
        return history.getAll()
    }

    override suspend fun clearHistory() {
        history.clear()
    }

    override suspend fun getCached(request: TranslationRequest): TranslationResult? {
        return cache.get(request)
    }
}
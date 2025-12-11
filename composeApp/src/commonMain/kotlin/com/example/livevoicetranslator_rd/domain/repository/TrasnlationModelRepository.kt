package com.example.livevoicetranslator_rd.domain.repository

import com.example.livevoicetranslator_rd.domain.model.TranslationModelItem
import kotlinx.coroutines.flow.Flow

interface TranslationModelRepository {

    suspend fun isModelDownloaded(lang: String): Boolean
    suspend fun detectLanguage(text: String): String

    fun observeTranslationModelStatuses(): Flow<List<TranslationModelItem>>
    suspend fun downloadModel(languageCode: String)
}
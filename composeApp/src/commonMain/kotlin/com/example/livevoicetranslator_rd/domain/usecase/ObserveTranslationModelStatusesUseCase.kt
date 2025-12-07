package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.TranslationModelItem
import com.example.livevoicetranslator_rd.domain.repository.TranslationModelRepository
import kotlinx.coroutines.flow.Flow

class ObserveTranslationModelStatusesUseCase(
    private val repository: TranslationModelRepository
) {
    operator fun invoke(): Flow<List<TranslationModelItem>> = repository.observeTranslationModelStatuses()
}
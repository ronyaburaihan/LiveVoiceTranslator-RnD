package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.TranslationResult
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class GetHistoryUseCase(
    private val repo: TranslationRepository
) {
    suspend operator fun invoke(): List<TranslationResult> {
        return repo.getHistory()
    }
}
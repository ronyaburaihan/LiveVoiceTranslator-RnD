package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class GetHistoryUseCase(
    private val repo: TranslationRepository
) {
    suspend operator fun invoke(): List<OrchestratorResult> {
        return repo.getHistory()
    }
}
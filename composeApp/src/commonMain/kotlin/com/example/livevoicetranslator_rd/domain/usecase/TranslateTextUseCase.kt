package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class TranslateTextUseCase(
    private val repo: TranslationRepository
) {
    suspend operator fun invoke(
        request: TranslationRequest
    ): OrchestratorResult {
        return repo.translate(request)
    }
}

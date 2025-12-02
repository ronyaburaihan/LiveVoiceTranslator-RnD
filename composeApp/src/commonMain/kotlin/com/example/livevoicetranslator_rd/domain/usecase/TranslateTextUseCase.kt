package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.model.TranslationResult
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class TranslateTextUseCase(
    private val repo: TranslationRepository
) {
    suspend operator fun invoke(
        request: TranslationRequest
    ): TranslationResult {
        return repo.translate(request)
    }
}

package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class SaveFavoriteUseCase(private val repo: TranslationRepository) {
    suspend operator fun invoke(result: OrchestratorResult) {
        repo.saveFavorite(result)
    }
}

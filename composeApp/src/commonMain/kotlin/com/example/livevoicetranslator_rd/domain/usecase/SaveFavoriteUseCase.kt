package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.TranslationResult
import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class SaveFavoriteUseCase(private val repo: TranslationRepository) {
    suspend operator fun invoke(result: TranslationResult) {
        repo.saveFavorite(result)
    }
}

package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.repository.TranslationRepository

class DetectLanguageUseCase(
    private val repo: TranslationRepository
) {
    suspend operator fun invoke(text: String): String {
        return repo.detectLanguage(text)
    }
}

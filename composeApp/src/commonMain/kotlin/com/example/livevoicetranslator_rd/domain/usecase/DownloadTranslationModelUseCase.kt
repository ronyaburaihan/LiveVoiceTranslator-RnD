package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.repository.TranslationModelRepository

class DownloadTranslationModelUseCase(
    private val repository: TranslationModelRepository
) {
    suspend operator fun invoke(languageCode: String) {
        repository.downloadModel(languageCode)
    }
}
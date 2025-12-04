package com.example.livevoicetranslator_rd.domain.usecase.tranlate


import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.TranslatorRepository

class TranslatorTextUseCase(private val repository: TranslatorRepository) {
    suspend operator fun invoke(
        text: String,
        sourceLang: String,
        targetLang: String
    ): SpeachResult<String> {
        return repository.translate(text, sourceLang, targetLang)
    }
}


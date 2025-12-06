package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.TextToSpeechRepository


class InitializeTTSUseCase(
    private val textToSpeechRepository: TextToSpeechRepository
) {
    suspend operator fun invoke(): SpeachResult<Unit> {
        return textToSpeechRepository.initialize()
    }
}
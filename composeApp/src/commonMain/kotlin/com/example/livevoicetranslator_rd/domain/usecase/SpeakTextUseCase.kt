package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.TextToSpeechRepository

class SpeakTextUseCase(
    private val textToSpeechRepository: TextToSpeechRepository
) {
    suspend operator fun invoke(text: String): SpeachResult<Unit> {
        return textToSpeechRepository.speak(text)
    }
}
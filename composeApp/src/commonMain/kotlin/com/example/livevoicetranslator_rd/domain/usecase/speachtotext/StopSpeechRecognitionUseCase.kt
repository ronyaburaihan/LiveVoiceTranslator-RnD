package com.example.livevoicetranslator_rd.domain.usecase.speachtotext

import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.SpeechToTextRepository

class StopSpeechRecognitionUseCase(
    private val speechToTextRepository: SpeechToTextRepository
) {
    suspend operator fun invoke(): SpeachResult<Unit> {
        return speechToTextRepository.stopListening()
    }
}
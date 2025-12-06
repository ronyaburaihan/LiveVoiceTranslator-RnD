package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.SpeechToTextRepository

class StartSpeechRecognitionUseCase(
    private val speechToTextRepository: SpeechToTextRepository
) {
    suspend operator fun invoke(): SpeachResult<Unit> {
        return speechToTextRepository.startListening()
    }
}
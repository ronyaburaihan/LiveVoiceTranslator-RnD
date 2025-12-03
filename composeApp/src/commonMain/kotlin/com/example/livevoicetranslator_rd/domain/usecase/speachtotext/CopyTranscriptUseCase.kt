package com.example.livevoicetranslator_rd.domain.usecase.speachtotext

import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.SpeechToTextRepository

class CopyTranscriptUseCase(
    private val speechToTextRepository: SpeechToTextRepository
) {
    suspend operator fun invoke(text: String): SpeachResult<Unit> {
        return speechToTextRepository.copyText(text)
    }
}
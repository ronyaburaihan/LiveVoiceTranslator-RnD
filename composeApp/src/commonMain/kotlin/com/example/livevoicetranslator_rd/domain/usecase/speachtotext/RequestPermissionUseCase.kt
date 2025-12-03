package com.example.livevoicetranslator_rd.domain.usecase.speachtotext

import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.SpeechToTextRepository

class RequestPermissionUseCase(
    private val speechToTextRepository: SpeechToTextRepository
) {
    suspend operator fun invoke(): SpeachResult<Boolean> {
        return speechToTextRepository.requestPermission()
    }
}
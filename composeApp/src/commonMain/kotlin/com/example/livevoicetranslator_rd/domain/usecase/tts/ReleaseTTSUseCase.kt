package com.example.livevoicetranslator_rd.domain.usecase


import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.TextToSpeechRepository

class ReleaseTTSUseCase(
    private val repository: TextToSpeechRepository
) {
    operator fun invoke(): SpeachResult<Unit> = repository.release()
}

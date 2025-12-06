package com.example.livevoicetranslator_rd.domain.usecase

import com.example.livevoicetranslator_rd.domain.model.TTSText
import com.example.livevoicetranslator_rd.domain.repository.TextToSpeechRepository
import kotlinx.coroutines.flow.Flow

class ObserveTTSStateUseCase(
    private val repository: TextToSpeechRepository
) {
    operator fun invoke(): Flow<TTSText> = repository.getTTSState()
}

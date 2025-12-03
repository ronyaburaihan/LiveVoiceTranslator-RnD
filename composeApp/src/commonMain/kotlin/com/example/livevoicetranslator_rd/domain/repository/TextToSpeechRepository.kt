package com.example.livevoicetranslator_rd.domain.repository

//import com.example.livevoicetranslator_rd.domain.model.Result
import com.example.livevoicetranslator_rd.domain.model.TTSText
import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import kotlinx.coroutines.flow.Flow

interface TextToSpeechRepository {
    fun getTTSState(): Flow<TTSText>
    fun speak(text: String): SpeachResult<Unit>
    fun pause(): SpeachResult<Unit>
    fun resume(): SpeachResult<Unit>
    fun stop(): SpeachResult<Unit>
    fun initialize(): SpeachResult<Unit>
    fun release(): SpeachResult<Unit>
}
package com.example.livevoicetranslator_rd.domain.repository

import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.model.speachtotext.Transcript
import kotlinx.coroutines.flow.Flow

interface SpeechToTextRepository {
    fun getTranscript(): Flow<Transcript>
    fun startListening(): SpeachResult<Unit>
    fun stopListening(): SpeachResult<Unit>
    suspend fun requestPermission(): SpeachResult<Boolean>
    fun getSupportedLanguages(): SpeachResult<List<String>>
    fun setLanguage(languageCode: String): SpeachResult<Unit>
    fun copyText(text: String): SpeachResult<Unit>
}
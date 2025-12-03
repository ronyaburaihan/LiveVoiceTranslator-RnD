package com.example.livevoicetranslator_rd.data.repository

import com.example.livevoicetranslator_rd.core.platform.SpeechToText
import com.example.livevoicetranslator_rd.domain.model.speachtotext.PermissionRequestStatus
import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.model.speachtotext.Transcript
import com.example.livevoicetranslator_rd.domain.repository.SpeechToTextRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpeechToTextRepositoryImpl(
    private val speechToText: SpeechToText
) : SpeechToTextRepository {

    override fun getTranscript(): Flow<Transcript> {
        return speechToText.transcriptState.map { transcriptState ->
            Transcript(
                text = transcriptState.transcript ?: "",
                isFinal = transcriptState.listeningStatus.name == "FINAL",
                confidence = 0.9f, // Default confidence
                timestamp = 0L // Platform-specific timestamp will be set
            )
        }
    }

    override fun startListening(): SpeachResult<Unit> {
        return try {
            speechToText.startTranscribing()
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }

    override fun stopListening(): SpeachResult<Unit> {
        return try {
            speechToText.stopTranscribing()
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }

    override suspend fun requestPermission(): SpeachResult<Boolean> {
        return try {
            speechToText.requestPermission { status ->
                when (status) {
                    PermissionRequestStatus.ALLOWED -> {
                        startListening()
                    }

                    PermissionRequestStatus.NOT_ALLOWED -> {
                        println("Permission denied")
                    }

                    PermissionRequestStatus.NEVER_ASK_AGAIN -> {
                        speechToText.showNeedPermission()
                    }
                }
                println("Permission status: $status")
            }
            SpeachResult.Success(true)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }

    override fun getSupportedLanguages(): SpeachResult<List<String>> {
        return try {
            SpeachResult.Success(listOf("bd-bn", "en-US", "es-ES", "fr-FR", "de-DE", "it-IT"))
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }

    override fun setLanguage(languageCode: String): SpeachResult<Unit> {
        return try {
            speechToText.setLanguage(languageCode)
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }

    override fun copyText(text: String): SpeachResult<Unit> {
        return try {
            speechToText.copyText(text)
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }
}

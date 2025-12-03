package com.example.livevoicetranslator_rd.data.repository

import com.example.livevoicetranslator_rd.core.platform.getTTSProvider
import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.model.TTSText
import com.example.livevoicetranslator_rd.domain.repository.TextToSpeechRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TextToSpeechRepositoryImpl : TextToSpeechRepository {
    
    private val ttsManager = getTTSProvider()
    private val _ttsState = MutableStateFlow(
        TTSText(
            text = "",
            highlightStart = 0,
            highlightEnd = 0,
            isPlaying = false,
            isPaused = false
        )
    )
    
    override fun getTTSState(): Flow<TTSText> = _ttsState.asStateFlow()
    
    override fun speak(text: String): SpeachResult<Unit> {
        return try {
            ttsManager.speak(
                text = text,
                onWordBoundary = { start, end ->
                    _ttsState.value = _ttsState.value.copy(
                        text = text,
                        highlightStart = start,
                        highlightEnd = end + 1,
                        isPlaying = true,
                        isPaused = false
                    )
                },
                onStart = {
                    _ttsState.value = _ttsState.value.copy(
                        text = text,
                        isPlaying = true,
                        isPaused = false
                    )
                },
                onComplete = {
                    _ttsState.value = _ttsState.value.copy(
                        highlightStart = 0,
                        highlightEnd = 0,
                        isPlaying = false,
                        isPaused = false
                    )
                }
            )
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }
    
    override fun pause(): SpeachResult<Unit> {
        return try {
            ttsManager.pause()
            _ttsState.value = _ttsState.value.copy(
                isPlaying = false,
                isPaused = true
            )
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }
    
    override fun resume(): SpeachResult<Unit> {
        return try {
            ttsManager.resume()
            _ttsState.value = _ttsState.value.copy(
                isPlaying = true,
                isPaused = false
            )
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }
    
    override fun stop(): SpeachResult<Unit> {
        return try {
            ttsManager.stop()
            _ttsState.value = _ttsState.value.copy(
                highlightStart = 0,
                highlightEnd = 0,
                isPlaying = false,
                isPaused = false
            )
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }
    
    override fun initialize(): SpeachResult<Unit> {
        return try {
            ttsManager.initialize { }
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }
    
    override fun release(): SpeachResult<Unit> {
        return try {
            ttsManager.release()
            SpeachResult.Success(Unit)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }
}
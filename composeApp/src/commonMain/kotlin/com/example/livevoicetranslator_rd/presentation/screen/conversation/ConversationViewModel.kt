package com.example.livevoicetranslator_rd.presentation.screen.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livevoicetranslator_rd.core.platform.SpeechToText
import com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus
import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.CopyTranscriptUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.RequestPermissionUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.StartSpeechRecognitionUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.StopSpeechRecognitionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.time.Clock

class ConversationViewModel(
    private val speechToText: SpeechToText,
    private val startSpeechRecognitionUseCase: StartSpeechRecognitionUseCase,
    private val stopSpeechRecognitionUseCase: StopSpeechRecognitionUseCase,
    private val requestPermissionUseCase: RequestPermissionUseCase,
    private val copyTranscriptUseCase: CopyTranscriptUseCase
) : ViewModel() {

    val transcriptState = speechToText.transcriptState

    // UI state for conversation
    data class ConversationUiState(
        val messages: List<ConversationMessage> = emptyList(),
        val isLeftMicActive: Boolean = false,
        val isRightMicActive: Boolean = false,
        val leftLanguage: String = "English",
        val rightLanguage: String = "Spanish",
        val availableLanguages: List<String> = listOf("English", "Spanish", "French", "German", "Italian", "Portuguese", "Chinese", "Japanese", "Korean")
    )

    data class ConversationMessage(
        val sourceText: String,
        val translatedText: String,
        val isLeftSide: Boolean,
        val timestamp: Long =0L
    )

    private val _uiState = MutableStateFlow(ConversationUiState())
    val uiState: StateFlow<ConversationUiState> = _uiState

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent

    init {
        // Set initial languages
        speechToText.setLanguage("en-US")
        
        // Observe transcript updates for real-time display
        viewModelScope.launch {
            transcriptState.collect { state ->
                // Update UI with live transcription if needed
                if (state.listeningStatus == ListeningStatus.INACTIVE && !state.transcript.isNullOrEmpty()) {
                    // This will be handled in stopListening() when speech recognition completes
                }
            }
        }
    }

    fun onLeftMicClick() {
        println("ConversationViewModel: Left mic clicked, current status: ${transcriptState.value.listeningStatus}")
        if (transcriptState.value.listeningStatus == ListeningStatus.INACTIVE) {
            // Set left language for recognition
            val leftLangCode = getLanguageCode(_uiState.value.leftLanguage)
            println("ConversationViewModel: Setting language to $leftLangCode for left side")
            speechToText.setLanguage(leftLangCode)
            handlePermissionRequest()
            _uiState.update { it.copy(isLeftMicActive = true) }
        } else {
            stopListening()
            _uiState.update { it.copy(isLeftMicActive = false) }
        }
    }

    fun onRightMicClick() {
        println("ConversationViewModel: Right mic clicked, current status: ${transcriptState.value.listeningStatus}")
        if (transcriptState.value.listeningStatus == ListeningStatus.INACTIVE) {
            // Set right language for recognition
            val rightLangCode = getLanguageCode(_uiState.value.rightLanguage)
            println("ConversationViewModel: Setting language to $rightLangCode for right side")
            speechToText.setLanguage(rightLangCode)
            handlePermissionRequest()
            _uiState.update { it.copy(isRightMicActive = true) }
        } else {
            stopListening()
            _uiState.update { it.copy(isRightMicActive = false) }
        }
    }

    fun onLeftMicLongPress() {
        if (transcriptState.value.listeningStatus == ListeningStatus.INACTIVE) {
            val leftLangCode = getLanguageCode(_uiState.value.leftLanguage)
            speechToText.setLanguage(leftLangCode)
            handlePermissionRequest()
            _uiState.update { it.copy(isLeftMicActive = true) }
        }
    }

    fun onRightMicLongPress() {
        if (transcriptState.value.listeningStatus == ListeningStatus.INACTIVE) {
            val rightLangCode = getLanguageCode(_uiState.value.rightLanguage)
            speechToText.setLanguage(rightLangCode)
            handlePermissionRequest()
            _uiState.update { it.copy(isRightMicActive = true) }
        }
    }

    fun onMicReleased() {
        println("ConversationViewModel: Mic released, current status: ${transcriptState.value.listeningStatus}")
        if (transcriptState.value.listeningStatus != ListeningStatus.INACTIVE) {
            stopListening()
            _uiState.update { 
                it.copy(
                    isLeftMicActive = false,
                    isRightMicActive = false
                ) 
            }
        } else {
            println("ConversationViewModel: Mic released but no active listening")
            // Reset mic states anyway to ensure clean state
            _uiState.update { 
                it.copy(
                    isLeftMicActive = false,
                    isRightMicActive = false
                ) 
            }
        }
    }

    private fun handlePermissionRequest() {
        viewModelScope.launch {
            println("ConversationViewModel: Requesting permission...")
            when (val result = requestPermissionUseCase()) {
                is SpeachResult.Success -> {
                    println("ConversationViewModel: Permission result received: ${result.data}")
                    if (result.data) {
                        println("ConversationViewModel: Permission granted, starting listening...")
                        startListening()
                    } else {
                        println("ConversationViewModel: Permission denied")
                        _uiEvent.value = UiEvent.ShowSnackbar("Permission denied")
                        _uiState.update { 
                            it.copy(
                                isLeftMicActive = false,
                                isRightMicActive = false
                            ) 
                        }
                    }
                }
                is SpeachResult.Error -> {
                    println("ConversationViewModel: Permission request failed: ${result.exception?.message}")
                    _uiEvent.value = UiEvent.ShowSnackbar("Permission request failed")
                    _uiState.update { 
                        it.copy(
                            isLeftMicActive = false,
                            isRightMicActive = false
                        ) 
                    }
                }
                SpeachResult.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    private fun startListening() {
        viewModelScope.launch {
            println("ConversationViewModel: Starting speech recognition...")
            when (val result = startSpeechRecognitionUseCase()) {
                is SpeachResult.Success -> {
                    println("ConversationViewModel: Speech recognition started successfully")
                    speechToText.startTranscribing()
                }
                is SpeachResult.Error -> {
                    println("ConversationViewModel: Error starting speech recognition: ${result.exception?.message}")
                    _uiEvent.value = UiEvent.ShowSnackbar("Failed to start speech recognition: ${result.exception?.message ?: "Unknown error"}")
                    _uiState.update { 
                        it.copy(
                            isLeftMicActive = false,
                            isRightMicActive = false
                        ) 
                    }
                }
                SpeachResult.Loading -> {
                    // Handle loading state if needed
                    println("ConversationViewModel: Speech recognition is loading...")
                }
            }
        }
    }

    private fun stopListening() {
        viewModelScope.launch {
            println("ConversationViewModel: Stopping speech recognition...")
            when (val result = stopSpeechRecognitionUseCase()) {
                is SpeachResult.Success -> {
                    speechToText.stopTranscribing()
                    // Add the recognized text as a new message
                    transcriptState.value.transcript?.let { recognizedText ->
                        println("ConversationViewModel: Processing recognized text: '$recognizedText'")
                        if (recognizedText.isNotBlank()) {
                            addMessage(recognizedText)
                        } else {
                            _uiEvent.value = UiEvent.ShowSnackbar("No speech detected")
                        }
                    } ?: run {
                        println("ConversationViewModel: No transcript available")
                        _uiEvent.value = UiEvent.ShowSnackbar("No speech detected")
                    }
                }
                is SpeachResult.Error -> {
                    println("ConversationViewModel: Error stopping speech recognition: ${result.exception?.message}")
                    _uiEvent.value = UiEvent.ShowSnackbar("Failed to stop speech recognition")
                }
                SpeachResult.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    private fun addMessage(sourceText: String) {
        val isLeftSide = _uiState.value.isLeftMicActive
        val targetLanguage = if (isLeftSide) _uiState.value.rightLanguage else _uiState.value.leftLanguage
        val sourceLanguage = if (isLeftSide) _uiState.value.leftLanguage else _uiState.value.rightLanguage
        
        // Enhanced translation logic
        val translatedText = translateText(sourceText, sourceLanguage, targetLanguage)
        
        val newMessage = ConversationMessage(
            sourceText = sourceText,
            translatedText = translatedText,
            isLeftSide = isLeftSide
        )
        
        _uiState.update { currentState ->
            currentState.copy(messages = currentState.messages + newMessage)
        }
        
        // Log for debugging
        println("ConversationViewModel: Added message - '$sourceText' -> '$translatedText' (side: ${if (isLeftSide) "left" else "right"})")
    }
    
    private fun translateText(sourceText: String, sourceLanguage: String, targetLanguage: String): String {
        // Simple translation simulation - in a real app, this would call a translation API
        return when {
            sourceLanguage == targetLanguage -> sourceText // No translation needed
            sourceText.isBlank() -> ""
            else -> {
                // Simulate translation with language codes
                val sourceLangCode = getLanguageCode(sourceLanguage)
                val targetLangCode = getLanguageCode(targetLanguage)
                "[$targetLangCode] $sourceText"
            }
        }
    }

    fun onLeftLanguageSelected(language: String) {
        _uiState.update { it.copy(leftLanguage = language) }
    }

    fun onRightLanguageSelected(language: String) {
        _uiState.update { it.copy(rightLanguage = language) }
    }

    fun onClickCopy(text: String) {
        viewModelScope.launch {
            when (val result = copyTranscriptUseCase(text)) {
                is SpeachResult.Success -> {
                    _uiEvent.value = UiEvent.ShowSnackbar("Text copied to clipboard")
                }
                is SpeachResult.Error -> {
                    _uiEvent.value = UiEvent.ShowSnackbar("Failed to copy text")
                }
                SpeachResult.Loading -> {
                    // Handle loading state if needed
                }
            }
        }
    }

    fun onUiEventHandled() {
        _uiEvent.value = null
    }

    fun onDismissRequest() {
        speechToText.dismissPermissionDialog()
    }

    fun openAppSettings() {
        speechToText.openAppSettings()
    }

    fun clearConversation() {
        _uiState.update { it.copy(messages = emptyList()) }
        _uiEvent.value = UiEvent.ShowSnackbar("Conversation cleared")
    }

    private fun getLanguageCode(languageName: String): String {
        return when (languageName) {
            "English" -> "en-US"
            "Spanish" -> "es-ES"
            "French" -> "fr-FR"
            "German" -> "de-DE"
            "Italian" -> "it-IT"
            "Portuguese" -> "pt-PT"
            "Chinese" -> "zh-CN"
            "Japanese" -> "ja-JP"
            "Korean" -> "ko-KR"
            else -> "en-US"
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
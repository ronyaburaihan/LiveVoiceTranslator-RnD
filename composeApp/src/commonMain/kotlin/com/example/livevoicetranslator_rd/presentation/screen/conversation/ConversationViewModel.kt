package com.example.livevoicetranslator_rd.presentation.screen.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livevoicetranslator_rd.core.platform.SpeechToText
import com.example.livevoicetranslator_rd.domain.model.TTSState
import com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus
import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.usecase.InitializeTTSUseCase
import com.example.livevoicetranslator_rd.domain.usecase.ObserveTTSStateUseCase
import com.example.livevoicetranslator_rd.domain.usecase.ReleaseTTSUseCase
import com.example.livevoicetranslator_rd.domain.usecase.SpeakTextUseCase
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
    private val copyTranscriptUseCase: CopyTranscriptUseCase,
    private val initializeTTSUseCase: InitializeTTSUseCase,
    private val observeTTSStateUseCase: ObserveTTSStateUseCase,
    private val releaseTTSUseCase: ReleaseTTSUseCase,
    private val speakTextUseCase: SpeakTextUseCase,
) : ViewModel() {

    val transcriptState = speechToText.transcriptState

    // UI state for conversation
    data class ConversationUiState(
        val messages: List<ConversationMessage> = emptyList(),
        val isLeftMicActive: Boolean = false,
        val isRightMicActive: Boolean = false,
        val leftLanguage: String = "English",
        val rightLanguage: String = "Spanish",
        val _isInitialized: Boolean = false,
        val text: String = "",
        val highlightStart: Int = 0,
        val highlightEnd: Int = 0,
        val isPlaying: Boolean = false,
        val isPaused: Boolean = false,
        val availableLanguages: List<String> = listOf("English","Bengali","Hindi", "Spanish", "French", "German", "Italian", "Portuguese", "Chinese", "Japanese", "Korean", "Russian")
    )

    data class ConversationMessage(
        val sourceText: String,
        val translatedText: String,
        val isLeftSide: Boolean,
        val timestamp: Long =0L
    )

    private val _ttsState = MutableStateFlow(TTSState.IDLE)
    val ttsState: StateFlow<TTSState> = _ttsState
    private val _uiState = MutableStateFlow(ConversationUiState())
    val uiState: StateFlow<ConversationUiState> = _uiState

    private val _uiEvent = MutableStateFlow<UiEvent?>(null)
    val uiEvent: StateFlow<UiEvent?> = _uiEvent

    init {
        // Set initial languages
        speechToText.setLanguage("en-US")
        
        // Observe transcript updates for real-time display and automatic processing
        var previousStatus = ListeningStatus.INACTIVE
        var wasListening = false
        
        viewModelScope.launch {
            transcriptState.collect { state ->
                println("ConversationViewModel: Transcript state changed - Status: ${state.listeningStatus}, Transcript: '${state.transcript}', Mic states: Left=${_uiState.value.isLeftMicActive}, Right=${_uiState.value.isRightMicActive}")
                
                // Track if we were listening
                if (state.listeningStatus == ListeningStatus.LISTENING) {
                    wasListening = true
                }
                
                // Handle automatic stop (system stopped recognition)
                // This triggers when status changes from LISTENING to INACTIVE
                if (previousStatus == ListeningStatus.LISTENING && 
                    state.listeningStatus == ListeningStatus.INACTIVE && 
                    !state.transcript.isNullOrEmpty() &&
                    wasListening) {
                    
                    // Check if we were actively listening (mic was active)
                    if (_uiState.value.isLeftMicActive || _uiState.value.isRightMicActive) {
                        println("ConversationViewModel: System stopped recognition automatically, processing text: '${state.transcript}'")
                        // Process the recognized text
                        processRecognizedText(state.transcript)
                        // Reset mic states
                        _uiState.update { 
                            it.copy(
                                isLeftMicActive = false,
                                isRightMicActive = false
                            ) 
                        }
                        wasListening = false
                    }
                }
                
                // Update previous status for next iteration
                previousStatus = state.listeningStatus
            }
        }
        initializeTTS()
        getSupportedLanguages()
    }

    private fun getSupportedLanguages() {
        speechToText.getSupportedLanguages { languages ->
            if (languages.isNotEmpty()) {
                _uiState.update { it.copy(availableLanguages = languages) }
            }
        }
    }

    fun onLeftMicClick() {
        println("ConversationViewModel: Left mic clicked, current status: ${transcriptState.value.listeningStatus}")
        if (transcriptState.value.listeningStatus == ListeningStatus.INACTIVE) {
            // Set left language for recognition
            val leftLangCode = _uiState.value.leftLanguage
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
            val rightLangCode = _uiState.value.rightLanguage
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
            val leftLangCode = _uiState.value.leftLanguage
            speechToText.setLanguage(leftLangCode)
            handlePermissionRequest()
            _uiState.update { it.copy(isLeftMicActive = true) }
        }
    }

    fun onRightMicLongPress() {
        if (transcriptState.value.listeningStatus == ListeningStatus.INACTIVE) {
            val rightLangCode = _uiState.value.rightLanguage
            speechToText.setLanguage(rightLangCode)
            handlePermissionRequest()
            _uiState.update { it.copy(isRightMicActive = true) }
        }
    }

    fun onMicReleased() {
        println("ConversationViewModel: Mic released, current status: ${transcriptState.value.listeningStatus}")
        println("ConversationViewModel: Current mic states - Left: ${_uiState.value.isLeftMicActive}, Right: ${_uiState.value.isRightMicActive}")
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

        private fun processRecognizedText(recognizedText: String) {
        println("ConversationViewModel: Processing recognized text: '$recognizedText'")
        if (recognizedText.isNotBlank()) {
            addMessage(recognizedText)
        } else {
            _uiEvent.value = UiEvent.ShowSnackbar("No speech detected")
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
                        // println("ConversationViewModel: Processing recognized text: '$recognizedText'")
                        // if (recognizedText.isNotBlank()) {
                        //     addMessage(recognizedText)
                        // } else {
                        //     _uiEvent.value = UiEvent.ShowSnackbar("No speech detected")
                        // }
                         processRecognizedText(recognizedText)
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
               // val sourceLangCode = getLanguageCode(sourceLanguage)
               // val targetLangCode = getLanguageCode(targetLanguage)
                "[$targetLanguage] $sourceText"
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

    private fun initializeTTS() {
        viewModelScope.launch {
            when (val result = initializeTTSUseCase()) {
                is SpeachResult.Success -> {
                    _uiState.value.copy(_isInitialized = true)
                }
                is SpeachResult.Error -> {
                    _uiState.value.copy(_isInitialized = false)
                }
                SpeachResult.Loading -> {
                }
            }
        }
    }
    fun speak(text: String) {
        if (text.isBlank()) return

        // Allow replaying after completion, but prevent multiple calls while playing
        if (_ttsState.value == TTSState.PLAYING) {
            println("TextToSpeechViewModel: Already playing, ignoring duplicate call")
            return
        }

        println("TextToSpeechViewModel: speak($text)")

        viewModelScope.launch {
            when (val result = speakTextUseCase(text)) {
                is SpeachResult.Success -> {
                    // State will be updated through repository observation
                    println("TextToSpeechViewModel: TTS started successfully")
                }
                is SpeachResult.Error -> {
                    _ttsState.value = TTSState.IDLE
                    _uiState.update {
                        it.copy(
                            isPlaying = false,
                            isPaused = false,
                            highlightStart = 0,
                            highlightEnd = 0
                        )
                    }
                    println("TextToSpeechViewModel: TTS error - ${result.exception}")
                }
                SpeachResult.Loading -> {
                    // Loading state - repository will update when ready
                    println("TextToSpeechViewModel: TTS loading...")
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

//    private fun getLanguageCode(languageName: String): String {
//        return when (languageName) {
//            "English" -> "en-US"
//            "Spanish" -> "es-ES"
//            "French" -> "fr-FR"
//            "German" -> "de-DE"
//            "Italian" -> "it-IT"
//            "Portuguese" -> "pt-PT"
//            "Chinese" -> "zh-CN"
//            "Japanese" -> "ja-JP"
//            "Korean" -> "ko-KR"
//            else -> "en-US"
//        }
//    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
package com.example.livevoicetranslator_rd.presentation.screen.conversation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livevoicetranslator_rd.core.platform.SpeechToText
import com.example.livevoicetranslator_rd.domain.model.TTSState
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus
import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.usecase.InitializeTTSUseCase
import com.example.livevoicetranslator_rd.domain.usecase.ObserveTTSStateUseCase
import com.example.livevoicetranslator_rd.domain.usecase.ReleaseTTSUseCase
import com.example.livevoicetranslator_rd.domain.usecase.SpeakTextUseCase
import com.example.livevoicetranslator_rd.domain.usecase.TranslateTextUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.CopyTranscriptUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.RequestPermissionUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.StartSpeechRecognitionUseCase
import com.example.livevoicetranslator_rd.domain.usecase.speachtotext.StopSpeechRecognitionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    private val translateTextUseCase: TranslateTextUseCase
) : ViewModel() {

    val transcriptState = speechToText.transcriptState

    // UI state for conversation
    data class ConversationUiState(
        val messages: List<ConversationMessage> = emptyList(),
        val liveText: String = "",
        val statusText: String = "",
        val isLeftMicActive: Boolean = false,
        val isRightMicActive: Boolean = false,
        val micPosition: Int = 1,
        val sourceLanguageCode: String = "en-US",
        val targetLanguageCode: String = "ar-SA",
        val leftLanguage: String = "English",
        val rightLanguage: String = "Spanish",
        val _isInitialized: Boolean = false,
        val text: String = "",
        val highlightStart: Int = 0,
        val highlightEnd: Int = 0,
        val isPlaying: Boolean = false,
        val isPaused: Boolean = false,
        val availableLanguages: List<String> = listOf(
            "English",
            "Bengali",
            "Hindi",
            "Spanish",
            "French",
            "German",
            "Italian",
            "Portuguese",
            "Chinese",
            "Japanese",
            "Korean",
            "Russian"
        )
    )

    data class ConversationMessage(
        val sourceText: String,
        val translatedText: String,
        val isLeftSide: Boolean,
        val timestamp: Long = 0L
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

//        // Observe transcript updates for real-time display and automatic processing
//        var previousStatus = ListeningStatus.INACTIVE
//        var wasListening = false
//
//        viewModelScope.launch {
//            transcriptState.collect { state ->
//                println("ConversationViewModel: Transcript state changed - Status: ${state.listeningStatus}, Transcript: '${state.transcript}', Mic states: Left=${_uiState.value.isLeftMicActive}, Right=${_uiState.value.isRightMicActive}")
//
//                // Track if we were listening
//                if (state.listeningStatus == ListeningStatus.LISTENING) {
//                    wasListening = true
//                }
//
//                // Handle automatic stop (system stopped recognition)
//                // This triggers when status changes from LISTENING to INACTIVE
//                if (previousStatus == ListeningStatus.LISTENING &&
//                    state.listeningStatus == ListeningStatus.INACTIVE &&
//                    !state.transcript.isNullOrEmpty() &&
//                    wasListening) {
//
//                    // Only process if we were actively listening (mic was active)
//                    if (_uiState.value.isLeftMicActive || _uiState.value.isRightMicActive) {
//                        println("ConversationViewModel: System stopped recognition automatically, processing text: '${state.transcript}'")
//                        // Process the recognized text
//                        processRecognizedText(state.transcript)
//                        // Keep mic active until user releases
//                        // _uiState.update {
//                        //     it.copy(
//                        //         isLeftMicActive = false,
//                        //         isRightMicActive = false
//                        //     )
//                        // }
//                        wasListening = false
//                    }
//                }
//
//                // Update previous status for next iteration
//                previousStatus = state.listeningStatus
//            }
//        }
        initializeTTS()
        getSupportedLanguages()
        observeStateOfListening()
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
            _uiState.update { it.copy(isLeftMicActive = true) }
            _uiState.update { it.copy(micPosition = 1) }
            val leftLangCode = _uiState.value.leftLanguage
            println("ConversationViewModel: Setting language to $leftLangCode for left side")
            speechToText.setLanguage(leftLangCode)
            handlePermissionRequest()
        } else {
            stopListening()
            _uiState.update { it.copy(isLeftMicActive = false) }
        }
    }


    fun observeStateOfListening() {
        viewModelScope.launch {
            transcriptState.collect { state ->
                println("ConversationViewModel: Transcript state changed - Status: ${state.listeningStatus}, Transcript: '${state.transcript}', Mic states: Left=${_uiState.value.isLeftMicActive}, Right=${_uiState.value.isRightMicActive}")
                if (state.listeningStatus == ListeningStatus.INACTIVE) {
                    if (state.transcript != null) {
                        processRecognizedText(state.transcript!!)
                    }

                    _uiState.update { it.copy(liveText = " ") }
                    _uiState.update { it.copy(statusText = "Processing...") }
                    state.transcript = null
                    _uiState.update { it.copy(isLeftMicActive = false) }
                    _uiState.update { it.copy(isRightMicActive = false) }


                } else if (state.listeningStatus == ListeningStatus.LISTENING) {
                    _uiState.update { it.copy(liveText = state.transcript ?: "") }
                    _uiState.update { it.copy(statusText = "Listening...") }
                    _uiState.update { it.copy(isLeftMicActive = state.listeningStatus == ListeningStatus.LISTENING && _uiState.value.micPosition == 1) }
                    _uiState.update { it.copy(isRightMicActive = state.listeningStatus == ListeningStatus.LISTENING && _uiState.value.micPosition == 2) }
                }

            }
        }
    }

    fun onRightMicClick() {
        println("ConversationViewModel: Right mic clicked, current status: ${transcriptState.value.listeningStatus}")
        if (transcriptState.value.listeningStatus == ListeningStatus.INACTIVE) {
            _uiState.update { it.copy(isRightMicActive = true) }
            // Set right language for recognition
            val rightLangCode = _uiState.value.rightLanguage
            println("ConversationViewModel: Setting language to $rightLangCode for right side")
            speechToText.setLanguage(rightLangCode)
            handlePermissionRequest()
            _uiState.update { it.copy(micPosition = 2) }


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
                    _uiEvent.value =
                        UiEvent.ShowSnackbar("Failed to start speech recognition: ${result.exception?.message ?: "Unknown error"}")
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
            speechToText.stopTranscribing()
        }
    }

    private fun addMessage(sourceText: String) {
        val isLeftSide = if (_uiState.value.micPosition == 1) true else false
        _uiState.update {
            it.copy(
                targetLanguageCode =
                    if (_uiState.value.micPosition == 1) _uiState.value.rightLanguage else _uiState.value.leftLanguage
            )
        }
        _uiState.update {
            it.copy(
                sourceLanguageCode =
                    if (_uiState.value.micPosition == 1) _uiState.value.leftLanguage else _uiState.value.rightLanguage
            )
        }

        // Create message with placeholder for translation
        val newMessage = ConversationMessage(
            sourceText = sourceText,
            translatedText = "Translating...", // Placeholder
            isLeftSide = isLeftSide,
            timestamp = 0L
        )

        // Add message immediately to show source text
        _uiState.update { currentState ->
            currentState.copy(messages = currentState.messages + newMessage)
        }

        // Perform translation asynchronously
        viewModelScope.launch {
            val translatedText = translateText(
                sourceText,
                _uiState.value.sourceLanguageCode,
                _uiState.value.targetLanguageCode
            )

            // Update the message with translated text
            _uiState.update { currentState ->
                val updatedMessages = currentState.messages.map { message ->
                    if (message.timestamp == newMessage.timestamp && message.sourceText == sourceText) {
                        message.copy(translatedText = translatedText)
                    } else {
                        message
                    }
                }
                currentState.copy(messages = updatedMessages)
            }
            speak(translatedText, _uiState.value.targetLanguageCode)

            // Log for debugging
            println("ConversationViewModel: Added message - '$sourceText' -> '$translatedText' (side: ${if (isLeftSide) "left" else "right"})")
        }
    }

    private suspend fun translateText(
        sourceText: String,
        sourceLanguage: String,
        targetLanguage: String
    ): String {
        return when {
            sourceLanguage == targetLanguage -> sourceText // No translation needed
            sourceText.isBlank() -> ""
            else -> {
                val sourceLangCode = getLanguageCode(sourceLanguage)
                val targetLangCode = getLanguageCode(targetLanguage)

                try {
                    val request = TranslationRequest(
                        text = sourceText,
                        sourceLang = sourceLangCode,
                        targetLang = targetLangCode
                    )
                    val result = translateTextUseCase(request)
                    result.translated
                } catch (e: Exception) {
                    "Translation error: ${e.message ?: "Unknown error"}"
                }
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

    fun speak(text: String, languageCode: String) {
        if (text.isBlank()) return

        if (_ttsState.value == TTSState.PLAYING) {
            println("TextToSpeechViewModel: Already playing, ignoring duplicate call")
            return
        }

        viewModelScope.launch {
            when (val result = speakTextUseCase(text, languageCode)) {
                is SpeachResult.Success -> {
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

    private fun getLanguageCode(languageNameOrCode: String): String {
        val s = languageNameOrCode.trim()
        if (s.isEmpty()) return "en"
        val parts = s.split('-', '_')
        if (parts.size > 1) {
            val known =
                setOf("en", "bn", "hi", "es", "fr", "de", "it", "pt", "zh", "ja", "ko", "ru")
            val first = parts.first().lowercase()
            val last = parts.last().lowercase()
            return when {
                known.contains(last) -> last
                known.contains(first) -> first
                first.length == 2 -> first
                last.length == 2 -> last
                else -> first
            }
        }
        return when (s) {
            "English" -> "en"
            "Bengali" -> "bn"
            "Hindi" -> "hi"
            "Spanish" -> "es"
            "French" -> "fr"
            "German" -> "de"
            "Italian" -> "it"
            "Portuguese" -> "pt"
            "Chinese" -> "zh"
            "Japanese" -> "ja"
            "Korean" -> "ko"
            "Russian" -> "ru"
            else -> s.lowercase().take(2)
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}
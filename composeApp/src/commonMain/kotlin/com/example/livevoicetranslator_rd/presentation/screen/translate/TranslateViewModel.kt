package com.example.livevoicetranslator_rd.presentation.screen.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livevoicetranslator_rd.core.platform.ClipboardService
import com.example.livevoicetranslator_rd.core.platform.ShareService
import com.example.livevoicetranslator_rd.core.platform.TTSService
import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.model.TranslationResult
import com.example.livevoicetranslator_rd.domain.usecase.DetectLanguageUseCase
import com.example.livevoicetranslator_rd.domain.usecase.GetHistoryUseCase
import com.example.livevoicetranslator_rd.domain.usecase.SaveFavoriteUseCase
import com.example.livevoicetranslator_rd.domain.usecase.TranslateTextUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val clipboardService: ClipboardService,
    private val shareService: ShareService,
    private val ttsService: TTSService,
    private val translateTextUseCase: TranslateTextUseCase,
    private val detectLanguageUseCase: DetectLanguageUseCase,
    private val saveFavoriteUseCase: SaveFavoriteUseCase,
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(TranslateUiState())
    val uiState = _uiState.asStateFlow()

    private val inputFlow = MutableSharedFlow<String>(replay = 1)
    private var inputJob: Job? = null

    init {
        startObservingInput()
        loadInitialHistory()
    }

    private fun loadInitialHistory() {
        viewModelScope.launch {
            val history = getHistoryUseCase()
            _uiState.update { it.copy(history = history) }
        }
    }

    private fun startObservingInput() {
        inputJob?.cancel()
        inputJob = viewModelScope.launch {
            inputFlow
                .debounce(500)
                .distinctUntilChanged()
                .collect { text ->
                    handleAutoTranslate(text)
                }
        }
    }

    fun onInputChanged(text: String) {
        _uiState.update {
            it.copy(
                inputText = text,
                charCount = text.length,
                translatedText = null
            )
        }

        viewModelScope.launch { inputFlow.emit(text) }
    }

    fun onTargetLangChanged(lang: String) {
        _uiState.update { it.copy(targetLang = lang) }
    }

    private suspend fun handleAutoTranslate(text: String) {
        if (text.isBlank()) {
            _uiState.update {
                it.copy(
                    translatedText = null,
                    detectedLanguage = null,
                    isLoading = false
                )
            }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }

        val detectedLangResult = try {
            detectLanguageUseCase(text)
        } catch (e: Exception) {
            "und"
        } as LanguageDetectionResult

        _uiState.update {
            it.copy(
                languageDetectionResult = LanguageDetectionResult(
                    languageCode = detectedLangResult.languageCode,
                    confidence = detectedLangResult.confidence
                )
            )
        }

        val targetLang = uiState.value.targetLang

        try {
            val result = translateTextUseCase(
                TranslationRequest(
                    text = text,
                    sourceLang = detectedLangResult.languageCode,
                    targetLang = targetLang
                )
            )

            _uiState.update {
                it.copy(
                    translatedText = result.translated,
                    isLoading = false,
                    history = getHistoryUseCase()
                )
            }

        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = e.message ?: "Translation failed"
                )
            }
        }
    }

    fun manualSaveFavorite() {
        viewModelScope.launch {
            val state = _uiState.value
            val input = state.inputText
            val output = state.translatedText ?: return@launch

            val fav = TranslationResult(
                original = input,
                translated = output,
                sourceLang = state.detectedLanguage,
                targetLang = state.targetLang
            )

            saveFavoriteUseCase(fav)

            _uiState.update { it.copy(history = getHistoryUseCase()) }
        }
    }

    fun onCopyClicked() {
        val txt = uiState.value.translatedText ?: return
        clipboardService.copyToClipboard(txt)
    }

    fun onPasteClicked() {
        val pasted = clipboardService.pasteFromClipboard()
        onInputChanged(pasted)
    }

    fun onShareClicked() {
        val txt = uiState.value.translatedText ?: return
        shareService.share(txt, subject = "Translation")
    }

    fun onSpeakClicked() {
        viewModelScope.launch {
            val txt = uiState.value.translatedText ?: return@launch
            ttsService.speak(
                text = txt,
                lang = uiState.value.detectedLanguage ?: "en",
                rate = 1.0f,
                gender = null
            )
        }
    }

    val mlKitSupportedLanguages = mapOf(
        "af" to "Afrikaans",
        "ar" to "Arabic",
        "be" to "Belarusian",
        "bg" to "Bulgarian",
        "bn" to "Bengali",
        "ca" to "Catalan",
        "cs" to "Czech",
        "da" to "Danish",
        "de" to "German",
        "el" to "Greek",
        "en" to "English",
        "eo" to "Esperanto",
        "es" to "Spanish",
        "et" to "Estonian",
        "fa" to "Persian",
        "fi" to "Finnish",
        "fr" to "French",
        "ga" to "Irish",
        "gl" to "Galician",
        "gu" to "Gujarati",
        "he" to "Hebrew",
        "hi" to "Hindi",
        "hr" to "Croatian",
        "ht" to "Haitian Creole",
        "hu" to "Hungarian",
        "id" to "Indonesian",
        "is" to "Icelandic",
        "it" to "Italian",
        "ja" to "Japanese",
        "ka" to "Georgian",
        "ko" to "Korean",
        "lt" to "Lithuanian",
        "lv" to "Latvian",
        "mk" to "Macedonian",
        "mr" to "Marathi",
        "ms" to "Malay",
        "mt" to "Maltese",
        "nl" to "Dutch",
        "no" to "Norwegian",
        "pl" to "Polish",
        "pt" to "Portuguese",
        "ro" to "Romanian",
        "ru" to "Russian",
        "sk" to "Slovak",
        "sl" to "Slovenian",
        "sq" to "Albanian",
        "sr" to "Serbian",
        "sv" to "Swedish",
        "sw" to "Swahili",
        "ta" to "Tamil",
        "te" to "Telugu",
        "th" to "Thai",
        "tr" to "Turkish",
        "uk" to "Ukrainian",
        "ur" to "Urdu",
        "vi" to "Vietnamese",
        "zh" to "Chinese"
    )
}

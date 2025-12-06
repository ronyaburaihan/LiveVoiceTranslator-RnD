package com.example.livevoicetranslator_rd.presentation.screen.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livevoicetranslator_rd.core.platform.ClipboardService
import com.example.livevoicetranslator_rd.core.platform.ShareService
import com.example.livevoicetranslator_rd.core.platform.TTSService
import com.example.livevoicetranslator_rd.domain.model.LanguageDetectionResult
import com.example.livevoicetranslator_rd.domain.model.ModelDownloadState
import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.model.TranslationEngine
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

    init {
        observeInputChanges()
        loadInitialHistory()
    }

    private fun loadInitialHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(history = getHistoryUseCase()) }
        }
    }

    private fun observeInputChanges() {
        viewModelScope.launch {
            inputFlow
                .debounce(500) // live translation debounce
                .distinctUntilChanged()
                .collect { text -> handleAutoTranslate(text) }
        }
    }

    fun onInputChanged(text: String) {
        _uiState.update {
            it.copy(inputText = text, charCount = text.length, translatedText = null)
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

        _uiState.update {
            it.copy(
                isLoading = true,
                modelDownloadState = ModelDownloadState.Downloading(
                    source = _uiState.value.detectedLanguage ?: "und",
                    target = uiState.value.targetLang
                )
            )
        }

        val detectedLangResult = try {
            detectLanguageUseCase(text)
        } catch (_: Exception) {
            LanguageDetectionResult(languageCode = "und", confidence = 0f)
        }

        _uiState.update { it.copy(
            detectedLanguage = detectedLangResult.languageCode,
            languageDetectionResult = LanguageDetectionResult(
                languageCode = detectedLangResult.languageCode,
                confidence = detectedLangResult.confidence
            )
        ) }

        try {
            val result = translateTextUseCase(
                TranslationRequest(
                    text = text,
                    sourceLang = detectedLangResult.languageCode,
                    targetLang = uiState.value.targetLang
                )
            )

            _uiState.update {
                it.copy(
                    translatedText = result.translatedText,
                    modelDownloadState = ModelDownloadState.Completed(
                        _uiState.value.detectedLanguage ?: "und", uiState.value.targetLang
                    ),
                    isLoading = false,
                    history = getHistoryUseCase(),
                    detectedEngine = result.engine
                )
            }

        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    modelDownloadState = ModelDownloadState.Failed(
                        _uiState.value.detectedLanguage ?: "und", uiState.value.targetLang, e.message
                    ),
                    error = e.message
                )
            }
        }
    }

    fun manualSaveFavorite() {
        viewModelScope.launch {
            val state = _uiState.value
            val output = state.translatedText ?: return@launch
            saveFavoriteUseCase(
                result = OrchestratorResult(
                    success = true,
                    engine = state.detectedEngine ?: TranslationEngine.AI,
                    translatedText = output
                )
            )
            _uiState.update { it.copy(history = getHistoryUseCase()) }
        }
    }

    fun onCopyClicked() = uiState.value.translatedText?.let { clipboardService.copyToClipboard(it) }
    fun onPasteClicked() = clipboardService.pasteFromClipboard().let { onInputChanged(it) }
    fun onShareClicked() =
        uiState.value.translatedText?.let { shareService.share(it, "Translation") }

    fun onSpeakClicked() = viewModelScope.launch {
        uiState.value.translatedText?.let {
            ttsService.speak(it, uiState.value.detectedLanguage ?: "en", 1.0f, null)
        }
    }
}

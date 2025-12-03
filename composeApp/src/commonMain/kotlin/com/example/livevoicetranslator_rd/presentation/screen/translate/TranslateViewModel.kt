package com.example.livevoicetranslator_rd.presentation.screen.translate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.model.TranslationResult
import com.example.livevoicetranslator_rd.domain.usecase.DetectLanguageUseCase
import com.example.livevoicetranslator_rd.domain.usecase.GetHistoryUseCase
import com.example.livevoicetranslator_rd.domain.usecase.SaveFavoriteUseCase
import com.example.livevoicetranslator_rd.domain.usecase.TranslateTextUseCase
import com.example.livevoicetranslator_rd.presentation.screen.translate.TranslateUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translateTextUseCase: TranslateTextUseCase,
    private val detectLanguageUseCase: DetectLanguageUseCase,
    private val saveFavoriteUseCase: SaveFavoriteUseCase,
    private val getHistoryUseCase: GetHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TranslateUiState())
    val uiState = _uiState.asStateFlow()

    private val debouncePeriod = 300L

    private var detectJob: Job? = null

    fun getHistory(){
        viewModelScope.launch {
            val result = getHistoryUseCase()
            _uiState.update { it.copy(history = result) }
        }
    }
    fun onInputChanged(text: String) {
        _uiState.update { it.copy(inputText = text, charCount = text.length) }

        detectJob?.cancel()
        detectJob = viewModelScope.launch {
            delay(debouncePeriod)
            val detected = detectLanguageUseCase(text)
            _uiState.update { it.copy(detectedLanguage = detected) }
        }
    }

    fun translate()  {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val request = TranslationRequest(
                text = uiState.value.inputText,
                sourceLang = uiState.value.detectedLanguage,
                targetLang = "en"
            )
            val result = translateTextUseCase(request)
            _uiState.update {
                it.copy(translatedText = result.translated, isLoading = false)
            }
        }
    }

    fun saveToFavorites()  {
        viewModelScope.launch {
            saveFavoriteUseCase(
                TranslationResult(
                    original = uiState.value.inputText,
                    translated = uiState.value.translatedText ?: ""
                )
            )
        }
    }
}

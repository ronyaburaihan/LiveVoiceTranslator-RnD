package com.example.livevoicetranslator_rd.presentation.screen.camera.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livevoicetranslator_rd.domain.model.TranslatableLanguages
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.usecase.DetectLanguageUseCase
import com.example.livevoicetranslator_rd.domain.usecase.TranslateTextUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultViewModel(
    private val translateTextUseCase: TranslateTextUseCase,
    private val detectLanguageUseCase: DetectLanguageUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState = _uiState.asStateFlow()

    fun detectLanguage(text: String) {
        viewModelScope.launch {
            val language = detectLanguageUseCase(text)
            val lang = TranslatableLanguages.entries
                .firstOrNull { it.code == language.languageCode }
                ?: TranslatableLanguages.English
            _uiState.update { it.copy(sourceLanguage = lang) }
        }
    }

    fun updateImageBytes(imageBytes: ByteArray?) {
        _uiState.update { it.copy(imageBytes = imageBytes) }
    }

    suspend fun translatePart(text: String): String {
        //_uiState.update { it.copy(isLoading = true) }

        val request = TranslationRequest(
            text = text,
            sourceLang = uiState.value.sourceLanguage.code,
            targetLang = uiState.value.targetLanguage.code
        )

        val result = translateTextUseCase(request)

        //_uiState.update { it.copy(isLoading = false) }

        return result.translatedText!!
    }

    fun updateTargetLanguage(targetLang: String) {
        val lang = TranslatableLanguages.entries
            .firstOrNull { it.code == targetLang }
            ?: TranslatableLanguages.English

        _uiState.update { it.copy(targetLanguage = lang) }
    }

    fun swapLanguages() {
        val temp = uiState.value.sourceLanguage
        _uiState.update { it.copy(sourceLanguage = uiState.value.targetLanguage) }
        _uiState.update { it.copy(targetLanguage = temp) }
    }

    fun updateSourceLanguage(sourceLangCode: String) {
        val lang = TranslatableLanguages.entries
            .firstOrNull { it.code == sourceLangCode }
            ?: TranslatableLanguages.English

        _uiState.update { it.copy(sourceLanguage = lang) }
    }

}
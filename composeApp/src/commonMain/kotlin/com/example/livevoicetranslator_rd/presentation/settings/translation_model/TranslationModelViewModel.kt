package com.example.livevoicetranslator_rd.presentation.settings.translation_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.livevoicetranslator_rd.domain.model.TranslationModelStatus
import com.example.livevoicetranslator_rd.domain.usecase.DownloadTranslationModelUseCase
import com.example.livevoicetranslator_rd.domain.usecase.ObserveTranslationModelStatusesUseCase
import com.example.livevoicetranslator_rd.presentation.model.TranslateLanguageModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TranslationModelViewModel(
    private val observeStatuses: ObserveTranslationModelStatusesUseCase,
    private val downloadModelUseCase: DownloadTranslationModelUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TranslationModelUiState())
    val uiState: StateFlow<TranslationModelUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            observeStatuses()
                .collect { items ->
                    val uiItems = items.map { item ->
                        val langModel = TranslateLanguageModel.fromDomain(item.language)
                        TranslationModelItemUi(
                            language = langModel,
                            isDownloaded = item.status == TranslationModelStatus.DOWNLOADED,
                            isDownloading = item.status == TranslationModelStatus.DOWNLOADING
                        )
                    }
                    _uiState.value = TranslationModelUiState(items = uiItems)
                }
        }
    }

    fun onDownloadClick(languageCode: String) {
        viewModelScope.launch {
            try {
                downloadModelUseCase(languageCode)
            } catch (_: Exception) {
            }
        }
    }
}
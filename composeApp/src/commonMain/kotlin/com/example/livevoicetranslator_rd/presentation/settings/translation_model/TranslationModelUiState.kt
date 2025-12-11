package com.example.livevoicetranslator_rd.presentation.settings.translation_model

import androidx.compose.runtime.Stable
import com.example.livevoicetranslator_rd.presentation.model.TranslateLanguageModel

@Stable
data class TranslationModelUiState(
    val items: List<TranslationModelItemUi> = emptyList()
)

data class TranslationModelItemUi(
    val language: TranslateLanguageModel,
    val isDownloaded: Boolean,
    val isDownloading: Boolean
)
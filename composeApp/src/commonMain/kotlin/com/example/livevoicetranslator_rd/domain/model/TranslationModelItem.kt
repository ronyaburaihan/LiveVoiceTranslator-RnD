package com.example.livevoicetranslator_rd.domain.model

enum class TranslationModelStatus {
    DOWNLOADED,
    DOWNLOADING,
    NOT_DOWNLOADED
}

data class TranslationModelItem(
    val language: TranslateLanguage,
    val status: TranslationModelStatus
)
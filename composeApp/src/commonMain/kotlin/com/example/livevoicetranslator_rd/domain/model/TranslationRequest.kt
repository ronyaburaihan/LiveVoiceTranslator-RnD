package com.example.livevoicetranslator_rd.domain.model

data class TranslationRequest(
    val text: String,
    val sourceLang: String? = null,
    val targetLang: String,
)
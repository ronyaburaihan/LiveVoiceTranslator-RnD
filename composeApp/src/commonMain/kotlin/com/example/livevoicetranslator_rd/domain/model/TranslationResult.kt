package com.example.livevoicetranslator_rd.domain.model

import com.example.livevoicetranslator_rd.domain.util.currentTimeMillis

data class TranslationResult(
    val original: String,
    val translated: String,
    val sourceLang: String? = null,
    val targetLang: String? = null,
    val timestamp: Long = currentTimeMillis()
)
package com.example.livevoicetranslator_rd.domain.model

data class OrchestratorResult(
    val success: Boolean,
    val engine: TranslationEngine,
    val translatedText: String? = null,
    val error: Throwable? = null
)
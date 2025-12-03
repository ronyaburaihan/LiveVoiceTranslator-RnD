package com.example.livevoicetranslator_rd.data.source

interface MLTranslator {
    suspend fun detectLanguage(text: String): String
    suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String

    suspend fun downloadModelIfNeeded(source: String, target: String)
}

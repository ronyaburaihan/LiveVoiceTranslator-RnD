package com.example.livevoicetranslator_rd.data.source

interface AITranslationDataSource {
    /**
     * Translate text using AI model. Throws on failure.
     * Keep this fast / low-latency. Should be cancellable.
     */
    suspend fun translateAi(text: String, sourceLang: String?, targetLang: String): String

    /**
     * Optional: check whether AI supports given language pair (fast local check).
     * Return true if AI should be used by default.
     */
    fun supportsLanguage(sourceLang: String?, targetLang: String): Boolean
}
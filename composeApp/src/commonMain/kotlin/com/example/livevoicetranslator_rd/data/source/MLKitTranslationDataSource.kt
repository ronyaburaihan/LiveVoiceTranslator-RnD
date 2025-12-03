package com.example.livevoicetranslator_rd.data.source

expect class MLKitTranslationDataSource() : MLTranslator {
    override suspend fun detectLanguage(text: String): String
    override suspend fun translate(text: String, sourceLang: String, targetLang: String): String
    override suspend fun downloadModelIfNeeded(source: String, target: String)
}
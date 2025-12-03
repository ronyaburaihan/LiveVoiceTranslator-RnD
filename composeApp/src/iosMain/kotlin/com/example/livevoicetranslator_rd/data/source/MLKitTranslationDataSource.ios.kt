package com.example.livevoicetranslator_rd.data.source

actual class MLKitTranslationDataSource actual constructor() :
    MLTranslator {
    actual override suspend fun detectLanguage(text: String): String {
        return ""
    }

    actual override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String {
        return ""
    }
}
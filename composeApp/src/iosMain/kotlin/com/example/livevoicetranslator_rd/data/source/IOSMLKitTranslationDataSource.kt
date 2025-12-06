package com.example.livevoicetranslator_rd.data.source

class IOSMLKitTranslationDataSource : MLKitTranslationDataSource {
    override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String {
        TODO("Not yet implemented")
    }

    override suspend fun downloadModelIfNeeded(
        sourceLang: String,
        targetLang: String
    ): Boolean {
        TODO("Not yet implemented")
    }
}
package com.example.livevoicetranslator_rd.data.source

expect class MLKitTranslationDataSource() : MLTranslator {
    override suspend fun detectLanguage(text: String): String
    override suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String
    override suspend fun isModelDownloaded(sourceLang: String, targetLang: String): Boolean
    override suspend fun downloadModel(sourceLang: String, targetLang: String): Result<Unit>
    override suspend fun deleteModel(sourceLang: String, targetLang: String): Result<Unit>
}
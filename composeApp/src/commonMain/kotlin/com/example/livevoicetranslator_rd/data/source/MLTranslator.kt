package com.example.livevoicetranslator_rd.data.source

interface MLTranslator {
    suspend fun detectLanguage(text: String): String
    suspend fun translate(
        text: String,
        sourceLang: String,
        targetLang: String
    ): String
    
    // Model management
    suspend fun isModelDownloaded(sourceLang: String, targetLang: String): Boolean
    suspend fun downloadModel(sourceLang: String, targetLang: String): Result<Unit>
    suspend fun deleteModel(sourceLang: String, targetLang: String): Result<Unit>
}

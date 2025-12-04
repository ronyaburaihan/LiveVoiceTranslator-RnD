package com.example.livevoicetranslator_rd.core.platform

expect object TranslatorPlatform {
    suspend fun translate(text: String, sourceLang: String, targetLang: String): String
}


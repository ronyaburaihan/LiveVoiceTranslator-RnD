package com.example.livevoicetranslator_rd.data.source

interface CloudTranslationDataSource {

    fun translateOnline(
        text: String,
        sourceLang: String?,
        targetLang: String
    ): String {
        return text
    }
}
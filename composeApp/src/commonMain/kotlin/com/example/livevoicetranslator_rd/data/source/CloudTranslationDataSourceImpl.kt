package com.example.livevoicetranslator_rd.data.source

class CloudTranslationDataSourceImpl : CloudTranslationDataSource {
    override fun translateOnline(
        text: String,
        sourceLang: String?,
        targetLang: String
    ): String {
        // TODO integrate Cloud API
        return "$text (cloud-todo)"
    }
}
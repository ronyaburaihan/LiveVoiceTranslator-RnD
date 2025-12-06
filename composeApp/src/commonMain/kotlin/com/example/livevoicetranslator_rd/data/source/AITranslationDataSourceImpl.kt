package com.example.livevoicetranslator_rd.data.source

class AITranslationDataSourceImpl : AITranslationDataSource {
    override suspend fun translateAi(text: String, sourceLang: String?, targetLang: String) =
        "$text (AI TODO)"

    override fun supportsLanguage(sourceLang: String?, targetLang: String) = true
}
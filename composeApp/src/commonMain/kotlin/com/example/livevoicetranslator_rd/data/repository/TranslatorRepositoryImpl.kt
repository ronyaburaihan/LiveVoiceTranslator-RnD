package com.example.livevoicetranslator_rd.data.repository

import com.example.livevoicetranslator_rd.core.platform.TranslatorPlatform
import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult
import com.example.livevoicetranslator_rd.domain.repository.TranslatorRepository

class TranslatorRepositoryImpl : TranslatorRepository {
    override suspend fun translate(text: String, sourceLang: String, targetLang: String): SpeachResult<String> {
        return try {
            val output = if (text.isBlank() || sourceLang == targetLang) text else
                TranslatorPlatform.translate(text, sourceLang, targetLang)
            SpeachResult.Success(output)
        } catch (e: Exception) {
            SpeachResult.Error(e)
        }
    }
}

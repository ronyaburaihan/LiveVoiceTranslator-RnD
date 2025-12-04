package com.example.livevoicetranslator_rd.domain.repository

import com.example.livevoicetranslator_rd.domain.model.speachtotext.SpeachResult

interface TranslatorRepository {
    suspend fun translate(text: String, sourceLang: String, targetLang: String): SpeachResult<String>
}


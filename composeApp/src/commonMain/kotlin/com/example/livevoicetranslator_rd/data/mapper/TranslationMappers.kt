package com.example.livevoicetranslator_rd.data.mapper

import com.example.livevoicetranslator_rd.domain.model.TranslationResult

fun mapToResult(
    input: String,
    output: String,
    source: String?,
    target: String
): TranslationResult {
    return TranslationResult(
        original = input,
        translated = output,
        sourceLang = source,
        targetLang = target
    )
}
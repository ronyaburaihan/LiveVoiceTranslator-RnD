package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.TranslationRequest
import com.example.livevoicetranslator_rd.domain.model.TranslationResult


class LocalCacheDataSource {

    private val cache = mutableMapOf<Int, TranslationResult>()

    fun put(request: TranslationRequest, result: TranslationResult) {
        cache[request.hashCode()] = result
    }

    fun get(request: TranslationRequest): TranslationResult? {
        return cache[request.hashCode()]
    }
}
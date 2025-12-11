package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult
import com.example.livevoicetranslator_rd.domain.model.TranslationRequest


class LocalCacheDataSource {

    private val cache = mutableMapOf<Int, OrchestratorResult>()

    fun put(request: TranslationRequest, result: OrchestratorResult) {
        cache[request.hashCode()] = result
    }

    fun get(request: TranslationRequest): OrchestratorResult? {
        return cache[request.hashCode()]
    }
}
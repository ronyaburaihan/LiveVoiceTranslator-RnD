package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.TranslationResult


class HistoryDataSource {

    private val history = mutableListOf<TranslationResult>()

    fun add(item: TranslationResult) {
        history.add(0, item)
    }

    fun getAll(): List<TranslationResult> = history

    fun clear() {
        history.clear()
    }
}
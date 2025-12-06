package com.example.livevoicetranslator_rd.data.source

import com.example.livevoicetranslator_rd.domain.model.OrchestratorResult


class HistoryDataSource {

    private val history = mutableListOf<OrchestratorResult>()

    fun add(item: OrchestratorResult) {
        history.add(0, item)
    }

    fun getAll(): List<OrchestratorResult> = history

    fun clear() {
        history.clear()
    }
}
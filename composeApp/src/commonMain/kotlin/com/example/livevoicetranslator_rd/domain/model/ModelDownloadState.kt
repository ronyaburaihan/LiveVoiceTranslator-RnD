package com.example.livevoicetranslator_rd.domain.model

sealed class ModelDownloadState {
    object Idle : ModelDownloadState()
    data class Downloading(val source: String, val target: String) : ModelDownloadState()
    data class Completed(val source: String, val target: String) : ModelDownloadState()
    data class Failed(val source: String, val target: String, val reason: String?) : ModelDownloadState()
}
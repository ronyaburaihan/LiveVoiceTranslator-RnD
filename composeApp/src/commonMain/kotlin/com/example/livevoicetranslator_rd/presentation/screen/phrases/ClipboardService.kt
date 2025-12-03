package com.example.livevoicetranslator_rd.presentation.screen.phrases

// commonMain
expect class ClipboardService {
    fun copyToClipboard(text: String)
    fun pasteFromClipboard(): String
}

object ClipboardProvider {
    lateinit var instance: ClipboardService
}
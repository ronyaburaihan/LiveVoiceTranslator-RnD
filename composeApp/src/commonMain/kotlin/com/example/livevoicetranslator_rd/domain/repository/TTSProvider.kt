package com.example.livevoicetranslator_rd.domain.repository

interface TTSProvider {
    fun initialize(onInitialized: () -> Unit)
    fun speak(
        text: String,
        languageCode: String? = null,
        onWordBoundary: (Int, Int) -> Unit,
        onStart: () -> Unit,
        onComplete: () -> Unit
    )

    fun stop()
    fun pause()
    fun resume()
    fun isPlaying(): Boolean
    fun isPaused(): Boolean
    fun release()
}
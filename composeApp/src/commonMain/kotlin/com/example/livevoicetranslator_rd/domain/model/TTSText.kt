package com.example.livevoicetranslator_rd.domain.model

data class TTSText(
    val text: String,
    val highlightStart: Int = 0,
    val highlightEnd: Int = 0,
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false
)
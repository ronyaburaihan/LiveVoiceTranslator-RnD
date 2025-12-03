package com.example.livevoicetranslator_rd.presentation.screen.phrases

import androidx.compose.runtime.Composable

// commonMain
interface TTS {
    fun speak(text: String)
    fun shutdown() {}
}
@Composable
expect fun rememberTTS(): TTS
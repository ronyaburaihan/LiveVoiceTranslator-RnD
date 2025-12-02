package com.example.livevoicetranslator_rd.presentation.screen.phrases

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.livevoicetranslator_rd.AndroidTTS

@Composable
actual fun rememberTTS(): TTS {
    val context = LocalContext.current
    val tts = remember { AndroidTTS(context) }

    DisposableEffect(Unit) {
        onDispose { tts.shutdown() }
    }

    return tts
}
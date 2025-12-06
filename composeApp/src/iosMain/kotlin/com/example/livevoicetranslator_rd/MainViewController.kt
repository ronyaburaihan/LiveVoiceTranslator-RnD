package com.example.livevoicetranslator_rd

import androidx.compose.ui.window.ComposeUIViewController
import com.example.livevoicetranslator_rd.core.di.initKoin
import com.example.livevoicetranslator_rd.core.platform.setTTSProvider
import com.example.livevoicetranslator_rd.domain.repository.TTSProvider
import com.example.livevoicetranslator_rd.presentation.app.App
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardProvider
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardService

fun MainViewController(ttsProvider: TTSProvider) = ComposeUIViewController(
    configure = {
        setTTSProvider { ttsProvider }
        initKoin()
    }
) {
    ClipboardProvider.instance = ClipboardService()
    App()
}
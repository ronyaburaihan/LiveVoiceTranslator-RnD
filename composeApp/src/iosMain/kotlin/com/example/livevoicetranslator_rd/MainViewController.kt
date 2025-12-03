package com.example.livevoicetranslator_rd

import androidx.compose.ui.window.ComposeUIViewController
import com.example.livevoicetranslator_rd.core.di.initKoin
import com.example.livevoicetranslator_rd.presentation.app.App
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardProvider
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardService
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardProvider
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardService

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    ClipboardProvider.instance = ClipboardService()
    App() }
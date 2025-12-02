package com.example.livevoicetranslator_rd

import androidx.compose.ui.window.ComposeUIViewController
import com.example.livevoicetranslator_rd.presentation.app.App
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardProvider
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardService

fun MainViewController() = ComposeUIViewController {

    // Initialize clipboard service
    ClipboardProvider.instance = ClipboardService()

    App()
}
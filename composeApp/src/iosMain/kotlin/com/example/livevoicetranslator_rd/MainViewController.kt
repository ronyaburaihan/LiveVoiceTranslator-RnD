package com.example.livevoicetranslator_rd

import androidx.compose.ui.window.ComposeUIViewController
import com.example.livevoicetranslator_rd.core.di.initKoin
import com.example.livevoicetranslator_rd.presentation.app.App

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }
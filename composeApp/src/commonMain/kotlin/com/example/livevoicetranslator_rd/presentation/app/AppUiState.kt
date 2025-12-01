package com.example.livevoicetranslator_rd.presentation.app

import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute

data class AppUiState(
    val language: String = "en",
    val initialScreen: ScreenRoute? = null,
    val showSplashScreen: Boolean = true
)

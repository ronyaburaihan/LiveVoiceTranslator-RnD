package com.example.livevoicetranslator_rd.presentation.util

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.navigation.NavHostController
import com.example.livevoicetranslator_rd.presentation.app.AppState

val LocalNavController =
    staticCompositionLocalOf<NavHostController> { error("NavHostController must be provided") }
val LocalSnackBarHostState =
    staticCompositionLocalOf<SnackbarHostState> { error("SnackBarHostState must be provided") }
val LocalAppState =
    staticCompositionLocalOf<AppState?> { error("AppState must be provided") }
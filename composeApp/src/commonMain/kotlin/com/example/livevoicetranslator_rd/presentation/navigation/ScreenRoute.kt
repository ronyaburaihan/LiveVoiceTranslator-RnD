package com.example.livevoicetranslator_rd.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class ScreenRoute {

    @Serializable
    object Main : ScreenRoute()

    @Serializable
    object Conservation : ScreenRoute()

    @Serializable
    object Translate : ScreenRoute()

    @Serializable
    object Camera : ScreenRoute()

    @Serializable
    object Phrases : ScreenRoute()

    @Serializable
    object Premium : ScreenRoute()
}

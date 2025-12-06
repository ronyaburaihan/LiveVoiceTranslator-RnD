package com.example.livevoicetranslator_rd.presentation.navigation

import com.example.livevoicetranslator_rd.presentation.screen.phrases.CategoryData
import kotlinx.serialization.Serializable
import kotlin.io.encoding.Base64

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
    data class OCRResultScreen(
        val imageBase64: String
    ): ScreenRoute()

    @Serializable
    object Phrases : ScreenRoute()

    @Serializable
    data class PhraseDetail(
        val categoryTitle: String
    ) : ScreenRoute()

    @Serializable
    object Premium : ScreenRoute()

    @Serializable
    object OnBoardingScreen : ScreenRoute()

    @Serializable
    object Referral : ScreenRoute()

    @Serializable
    data class Settings(val title: String) : ScreenRoute()

}

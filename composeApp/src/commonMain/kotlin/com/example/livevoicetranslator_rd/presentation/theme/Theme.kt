package com.hashtag.generator.ai.post.writer.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.livevoicetranslator_rd.presentation.theme.BackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.ColorTertiary
import com.example.livevoicetranslator_rd.presentation.theme.LocalDimensions
import com.example.livevoicetranslator_rd.presentation.theme.OnBackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.OnSurfaceColor
import com.example.livevoicetranslator_rd.presentation.theme.OutlineColor
import com.example.livevoicetranslator_rd.presentation.theme.PoppinsTypography
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import com.example.livevoicetranslator_rd.presentation.theme.SecondaryColor
import com.example.livevoicetranslator_rd.presentation.theme.SurfaceColor
import com.example.livevoicetranslator_rd.presentation.theme.SurfaceContainer
import com.example.livevoicetranslator_rd.presentation.theme.rememberAppDimensions

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = ColorTertiary,
)

private val LightColorScheme = lightColorScheme(
    surface = SurfaceColor,
    surfaceContainer = SurfaceContainer,
    onSurface = OnSurfaceColor,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = ColorTertiary,
    outline = OutlineColor
)

@Composable
fun TurnerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val dimensions = rememberAppDimensions()

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(
        LocalDimensions provides dimensions
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = PoppinsTypography(),
            content = content
        )
    }
}
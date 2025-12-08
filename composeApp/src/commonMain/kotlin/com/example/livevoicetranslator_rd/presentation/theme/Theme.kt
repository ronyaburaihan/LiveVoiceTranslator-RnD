package com.example.livevoicetranslator_rd.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = ColorTertiary,
    surface = SurfaceColor,
    onSurface = OnSurfaceColor,
    background = BackgroundColor,
    onBackground = OnBackgroundColor,
    outline = OutlineColor,
    surfaceContainer = SurfaceContainer
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
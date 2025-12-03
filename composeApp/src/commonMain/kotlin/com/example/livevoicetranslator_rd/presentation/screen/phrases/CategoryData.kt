package com.example.livevoicetranslator_rd.presentation.screen.phrases

import org.jetbrains.compose.resources.DrawableResource

data class CategoryData(
    val id: String,
    val title: String,
    val iconRes: DrawableResource,
    val isLocked: Boolean = false
)
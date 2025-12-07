package com.example.livevoicetranslator_rd.presentation.screen.phrases

import androidx.compose.runtime.Composable

// commonMain/ShareManager.kt
expect class ShareManager {
    fun shareText(text: String, title: String? = null)
}

@Composable
expect fun rememberShareManager(): ShareManager
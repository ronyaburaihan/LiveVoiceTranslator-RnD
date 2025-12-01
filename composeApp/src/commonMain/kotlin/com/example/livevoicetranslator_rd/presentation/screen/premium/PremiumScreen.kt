package com.example.livevoicetranslator_rd.presentation.screen.premium

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PremiumScreen() {
    PremiumScreenContent()
}
@Composable
private fun PremiumScreenContent() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Premium Screen"
        )
    }
}

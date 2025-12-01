package com.example.livevoicetranslator_rd.presentation.screen.translate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TranslateScreen() {
    TranslateScreenContent()
}

@Composable
private fun TranslateScreenContent(){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Translate Screen"
        )
    }
}
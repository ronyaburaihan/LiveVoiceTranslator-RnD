package com.example.livevoicetranslator_rd.presentation.screen.phrases

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun PhrasesScreen() {
    PhrasesScreenContent()
}

@Composable
private fun PhrasesScreenContent(){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Phrases Screen"
        )
    }
}

package com.example.livevoicetranslator_rd.presentation.screen.conversation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun ConversationScreen() {
    ConversationScreenContent()
}

@Composable
private fun ConversationScreenContent(){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Conversation Screen"
        )
    }
}
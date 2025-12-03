package com.example.livevoicetranslator_rd.presentation.screen.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LanguageSelector(
    sourceLanguage: String,
    targetLanguage: String,
    onSourceLanguageClick: () -> Unit = {},
    onTargetLanguageClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(
                color = Color.Black.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        LanguageChip(
            language = sourceLanguage,
            onClick = onSourceLanguageClick
        )
        
        LanguageChip(
            language = targetLanguage,
            onClick = onTargetLanguageClick
        )
    }
}

@Composable
private fun LanguageChip(
    language: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = language,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Black
        )
    }
}

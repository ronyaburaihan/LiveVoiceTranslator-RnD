package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun SpeakButton(
    modifier: Modifier = Modifier,
    iconTint: Color = Color.Unspecified,
    onClick: () -> Unit,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick
    ) {
        Icon(
            Icons.AutoMirrored.Filled.VolumeUp,
            contentDescription = "Listen",
            tint = iconTint
        )
    }
}

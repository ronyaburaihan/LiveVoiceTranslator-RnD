package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor

@Composable
fun GeneralActionButton(
    icon: Painter,
    color: Color = PrimaryColor,
    contentDescription: String,
    onClick: () -> Unit,
    boxSize: Dp = 28.dp,
    iconSize: Dp = 20.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(boxSize)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = icon,
            contentDescription = contentDescription,
            tint = color
        )
    }
}
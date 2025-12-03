package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MicButton(
    color: Color,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onLongClickRelease: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Using Box with shadow to create the prominent floating effect
    Box(
        modifier = modifier
            .size(72.dp)
            .shadow(elevation = 8.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(color)
            .pointerInput(Unit) {
                var isLongPress = false
                detectTapGestures(
                    onTap = { onClick() },
                    onLongPress = {
                        isLongPress = true
                        onLongClick()
                    },
                    onPress = {
                        isLongPress = false
                        try {
                            awaitRelease()
                        } finally {
                            if (isLongPress) {
                                onLongClickRelease()
                            }
                        }
                    }
                )
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Speak",
            tint = Color.White,
            modifier = Modifier.size(32.dp)
        )
    }
}
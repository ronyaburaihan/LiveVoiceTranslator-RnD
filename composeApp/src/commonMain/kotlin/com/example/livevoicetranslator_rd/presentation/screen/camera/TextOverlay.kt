package com.example.livevoicetranslator_rd.presentation.screen.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.domain.model.BoundingBox
import com.example.livevoicetranslator_rd.domain.model.TextBlock

@Composable
fun TextOverlay(
    textBlocks: List<TextBlock>,
    translatedBlocks: Map<String, String> = emptyMap(),
    showTranslation: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        textBlocks.forEach { textBlock ->
            val displayText = if (showTranslation && translatedBlocks.containsKey(textBlock.text)) {
                translatedBlocks[textBlock.text] ?: textBlock.text
            } else {
                textBlock.text
            }
            
            textBlock.boundingBox?.let { boundingBox ->
                TextBlockOverlay(
                    text = displayText,
                    boundingBox = boundingBox,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
private fun TextBlockOverlay(
    text: String,
    boundingBox: BoundingBox,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .offset {
                IntOffset(
                    x = boundingBox.left.toInt(),
                    y = boundingBox.top.toInt()
                )
            }
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFFFFEB3B), // Yellow color
            modifier = Modifier
                .background(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = RoundedCornerShape(4.dp)
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardProvider
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardService
import com.example.livevoicetranslator_rd.presentation.screen.phrases.PhraseItem
import com.example.livevoicetranslator_rd.presentation.screen.phrases.TTS
import com.example.livevoicetranslator_rd.presentation.screen.phrases.rememberTTS
import com.example.livevoicetranslator_rd.presentation.theme.OnBackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.OnSurfaceColor
import com.example.livevoicetranslator_rd.presentation.theme.OutlineColor
import com.example.livevoicetranslator_rd.presentation.theme.PremiumButtonColor
import com.example.livevoicetranslator_rd.presentation.theme.SecondaryColor
import com.example.livevoicetranslator_rd.presentation.theme.textColour
import kotlinx.coroutines.delay
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_copy
import livevoicetranslatorrd.composeapp.generated.resources.ic_play_audio
import livevoicetranslatorrd.composeapp.generated.resources.ic_share
import org.jetbrains.compose.resources.painterResource

@Composable
fun PhraseCard(
    phrase: PhraseItem,
    onExpandClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    tts: TTS
) {
    val ttsInstance = tts

    PhraseCardContent(
        phrase = phrase,
        onExpandClick = onExpandClick,
        onFavoriteClick = onFavoriteClick,
        onSpeakerClick = { ttsInstance.speak(phrase.targetText) }
    )
}

@Composable
fun PhraseCardContent(
    phrase: PhraseItem,
    onExpandClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSpeakerClick: () -> Unit
) {
    var showCopiedMessage by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.onPrimary),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Original text (English)
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = phrase.sourceText,
                    modifier = Modifier.weight(1f),
                    color = textColour,
                    style = MaterialTheme.typography.bodyMedium
                )
                Icon(
                    if (phrase.isExpanded) Icons.Default.KeyboardArrowUp
                    else Icons.Default.KeyboardArrowDown,
                    contentDescription = if (phrase.isExpanded) "Collapse" else "Expand",
                    tint = OnBackgroundColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null // removes ripple
                        ) {
                            onExpandClick()
                        }
                )
            }

            // Expanded content (Translation + Actions)
            if (phrase.isExpanded && phrase.targetText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(13.dp))

                HorizontalDivider(
                    color = OutlineColor,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Translated text (Spanish)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = phrase.targetText,
                        modifier = Modifier.weight(1f),
                        color = SecondaryColor,
                        fontSize = 15.sp
                    )
//                    Icon(
//                        Icons.Default.KeyboardArrowUp,
//                        contentDescription = "Collapse",
//                        tint = Color(0xFF757575),
//                        modifier = Modifier.size(24.dp)
//                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action buttons row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Play audio button
                        IconButton(
                            onClick = onSpeakerClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center,
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_play_audio),
                                    contentDescription = "Copy",
                                    modifier = Modifier.size(20.dp),
                                    colorFilter = ColorFilter.tint(OnSurfaceColor)
                                )
                            }
                        }

                        // Copy button
                        IconButton(
                            onClick = {
                                if (phrase.targetText.isNotEmpty()) {
                                    ClipboardProvider.instance.copyToClipboard(phrase.targetText)
                                    showCopiedMessage = true
                                }
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_copy),
                                    contentDescription = "Copy",
                                    modifier = Modifier.size(20.dp),
                                    colorFilter = ColorFilter.tint(OnSurfaceColor)
                                )
                            }
                        }

                        // Share button
                        IconButton(
                            onClick = { },
                            modifier = Modifier.size(36.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(Res.drawable.ic_share),
                                    contentDescription = "Copy",
                                    modifier = Modifier.size(20.dp),
                                    colorFilter = ColorFilter.tint(OnSurfaceColor)
                                )
                            }
                        }
                    }

                    // Favorite button (right side)
                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            if (phrase.isFavorite) Icons.Filled.Star else Icons.Default.StarBorder,
                            contentDescription = "Favorite",
                            tint = if (phrase.isFavorite) PremiumButtonColor else OnSurfaceColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

package com.example.livevoicetranslator_rd.presentation.screen.translate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.domain.model.TranslatableLanguages
import com.example.livevoicetranslator_rd.presentation.theme.GrayIconColor
import com.example.livevoicetranslator_rd.presentation.theme.OutlineColor
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import org.koin.compose.viewmodel.koinViewModel

// ================================
// Main TranslateScreen Composable
// ================================
@Composable
fun TranslateScreen(
    viewModel: TranslateViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { paddingValues ->
        TranslateScreenContent(
            uiState = uiState,
            onInputChanged = viewModel::onInputChanged,
            onSourceLangChanged = viewModel::onSourceLangChanged,
            onTargetLangChanged = viewModel::onTargetLangChanged,
            onSwapLanguages = viewModel::onSwapLanguages,
            onPasteClicked = viewModel::onPasteClicked,
            onMicClicked = { /* TODO: Implement mic functionality */ },
            onSpeakClicked = viewModel::onSpeakClicked,
            onCopyClicked = viewModel::onCopyClicked,
            onShareClicked = viewModel::onShareClicked,
            onFavoriteClicked = viewModel::manualSaveFavorite,
            modifier = Modifier.padding(paddingValues)
        )
    }
}

// ================================
// TranslateScreenContent Composable
// ================================
@Composable
fun TranslateScreenContent(
    uiState: TranslateUiState,
    onInputChanged: (String) -> Unit,
    onSourceLangChanged: (String) -> Unit,
    onTargetLangChanged: (String) -> Unit,
    onSwapLanguages: () -> Unit,
    onPasteClicked: () -> Unit,
    onMicClicked: () -> Unit,
    onSpeakClicked: () -> Unit,
    onCopyClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Language Selector Row
        LanguageSelectorRow(
            sourceLang = uiState.sourceLang,
            targetLang = uiState.targetLang,
            onSourceLangChanged = onSourceLangChanged,
            onTargetLangChanged = onTargetLangChanged,
            onSwapLanguages = onSwapLanguages
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Source Input Card
        SourceInputCard(
            inputText = uiState.inputText,
            onInputChanged = onInputChanged,
            onPasteClicked = onPasteClicked,
            onMicClicked = onMicClicked
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Translated Output Card
        TranslatedOutputCard(
            translatedText = uiState.translatedText,
            charCount = uiState.charCount,
            isLoading = uiState.isLoading,
            onSpeakClicked = onSpeakClicked,
            onCopyClicked = onCopyClicked,
            onShareClicked = onShareClicked,
            onFavoriteClicked = onFavoriteClicked
        )
    }
}

// ================================
// Language Selector Row
// ================================
@Composable
private fun LanguageSelectorRow(
    sourceLang: String,
    targetLang: String,
    onSourceLangChanged: (String) -> Unit,
    onTargetLangChanged: (String) -> Unit,
    onSwapLanguages: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Source Language Dropdown
        LanguageDropdownButton(
            selectedLanguageCode = sourceLang,
            onLanguageSelected = onSourceLangChanged,
            modifier = Modifier.weight(1f)
        )

        // Swap Button
        IconButton(
            onClick = onSwapLanguages,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SwapHoriz,
                contentDescription = "Swap Languages",
                tint = PrimaryColor,
                modifier = Modifier.size(28.dp)
            )
        }

        // Target Language Dropdown
        LanguageDropdownButton(
            selectedLanguageCode = targetLang,
            onLanguageSelected = onTargetLangChanged,
            modifier = Modifier.weight(1f)
        )
    }
}

// ================================
// Language Dropdown Button
// ================================
@Composable
private fun LanguageDropdownButton(
    selectedLanguageCode: String,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    val languages = TranslatableLanguages.entries
    val selectedLanguage = languages.find { it.code == selectedLanguageCode }

    Box(modifier = modifier) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            onClick = { expanded = true },
            shape = RoundedCornerShape(22.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, OutlineColor)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedLanguage?.title ?: "Select",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { lang ->
                DropdownMenuItem(
                    text = { Text(lang.title) },
                    onClick = {
                        onLanguageSelected(lang.code)
                        expanded = false
                    }
                )
            }
        }
    }
}

// ================================
// Source Input Card
// ================================
@Composable
private fun SourceInputCard(
    inputText: String,
    onInputChanged: (String) -> Unit,
    onPasteClicked: () -> Unit,
    onMicClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            border = BorderStroke(2.dp, PrimaryColor)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Text Input Field
                TextField(
                    value = inputText,
                    onValueChange = onInputChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    placeholder = {
                        Text(
                            text = "Type or paste text here...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Bottom Row with Paste Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Paste Button
                    PasteButton(onClick = onPasteClicked)

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        // Floating Mic Button
        MicrophoneButton(
            onClick = onMicClicked,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp)
        )
    }
}

// ================================
// Paste Button
// ================================
@Composable
private fun PasteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        border = BorderStroke(1.dp, OutlineColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ContentPaste,
                contentDescription = null,
                tint = PrimaryColor,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Paste",
                style = MaterialTheme.typography.labelMedium,
                color = PrimaryColor
            )
        }
    }
}

// ================================
// Microphone Button
// ================================
@Composable
private fun MicrophoneButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(56.dp)
            .shadow(elevation = 8.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(PrimaryColor)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Mic,
            contentDescription = "Voice Input",
            tint = Color.White,
            modifier = Modifier.size(28.dp)
        )
    }
}

// ================================
// Translated Output Card
// ================================
@Composable
private fun TranslatedOutputCard(
    translatedText: String?,
    charCount: Int,
    isLoading: Boolean,
    onSpeakClicked: () -> Unit,
    onCopyClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, OutlineColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Translated Text Display
            Text(
                text = translatedText ?: "Translation will appear here...",
                style = MaterialTheme.typography.bodyLarge,
                color = if (translatedText != null)
                    MaterialTheme.colorScheme.onSurface
                else
                    MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom Action Row
            TranslationActionRow(
                charCount = charCount,
                onSpeakClicked = onSpeakClicked,
                onCopyClicked = onCopyClicked,
                onShareClicked = onShareClicked,
                onFavoriteClicked = onFavoriteClicked
            )
        }
    }
}

// ================================
// Translation Action Row
// ================================
@Composable
private fun TranslationActionRow(
    charCount: Int,
    onSpeakClicked: () -> Unit,
    onCopyClicked: () -> Unit,
    onShareClicked: () -> Unit,
    onFavoriteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Action Icons Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Speaker Button
            TranslationActionButton(
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                contentDescription = "Read aloud",
                onClick = onSpeakClicked
            )

            // Copy Button
            TranslationActionButton(
                icon = Icons.Default.ContentCopy,
                contentDescription = "Copy",
                onClick = onCopyClicked
            )

            // Share Button
            TranslationActionButton(
                icon = Icons.Default.Share,
                contentDescription = "Share",
                onClick = onShareClicked
            )

            // Favorite Button
            TranslationActionButton(
                icon = Icons.Default.Star,
                contentDescription = "Save to favorites",
                onClick = onFavoriteClicked
            )
        }

        // Character Counter
        Text(
            text = "$charCount/3000",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End
        )
    }
}

// ================================
// Translation Action Button
// ================================
@Composable
private fun TranslationActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier.size(40.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = GrayIconColor,
            modifier = Modifier.size(22.dp)
        )
    }
}

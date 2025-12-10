package com.example.livevoicetranslator_rd.presentation.screen.translate

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.domain.model.TranslatableLanguages
import com.example.livevoicetranslator_rd.presentation.component.MicButton
import com.example.livevoicetranslator_rd.presentation.theme.GrayIconColor
import com.example.livevoicetranslator_rd.presentation.theme.OutlineColor
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_arrow_two_way
import livevoicetranslatorrd.composeapp.generated.resources.ic_chevron_down
import livevoicetranslatorrd.composeapp.generated.resources.ic_copy
import livevoicetranslatorrd.composeapp.generated.resources.ic_copy_outline
import livevoicetranslatorrd.composeapp.generated.resources.ic_mic
import livevoicetranslatorrd.composeapp.generated.resources.ic_paste
import livevoicetranslatorrd.composeapp.generated.resources.ic_rating_star
import livevoicetranslatorrd.composeapp.generated.resources.ic_share
import livevoicetranslatorrd.composeapp.generated.resources.ic_share_outline
import livevoicetranslatorrd.composeapp.generated.resources.ic_star_filled
import livevoicetranslatorrd.composeapp.generated.resources.ic_voice_custom
import livevoicetranslatorrd.composeapp.generated.resources.ic_volume_outline
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TranslateScreen(
    viewModel: TranslateViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

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
        onFavoriteClicked = viewModel::manualSaveFavorite
    )
}

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
    onFavoriteClicked: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

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
                onMicClicked = onMicClicked,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(19.dp))

            // Translated Output Card
            TranslatedOutputCard(
                translatedText = uiState.translatedText,
                charCount = uiState.charCount,
                isLoading = uiState.isLoading,
                onSpeakClicked = onSpeakClicked,
                onCopyClicked = onCopyClicked,
                onShareClicked = onShareClicked,
                onFavoriteClicked = onFavoriteClicked,
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}


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

        IconButton(
            onClick = onSwapLanguages,
            modifier = Modifier
                .padding(start = 13.dp, end = 13.dp)
                .border(
                    BorderStroke(0.4.dp, Color(0xFFE2E2E2)),
                    shape = RoundedCornerShape(50.dp)
                )
                .size(40.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_two_way),
                contentDescription = "Swap Languages",
                tint = PrimaryColor,
                modifier = Modifier.size(18.dp)
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
                .height(40.dp),
            onClick = { expanded = true },
            shape = RoundedCornerShape(30.dp),
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(0.4.dp, Color( 0xFFE2E2E2))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedLanguage?.title ?: "Select",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(start = 20.5.dp)
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_chevron_down),
                    contentDescription = null,
                    tint = Color(0xFF333333),
                    modifier = Modifier.padding(end = 20.5.dp)
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { lang ->
                DropdownMenuItem(
                    text = { Text(
                        text = lang.title,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color(0xFF333333)
                    ) },
                    onClick = {
                        onLanguageSelected(lang.code)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
private fun SourceInputCard(
    inputText: String,
    onInputChanged: (String) -> Unit,
    onPasteClicked: () -> Unit,
    onMicClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, Color(0xFFEAEAEA)),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ) {
        BasicTextField(
            value = inputText,
            onValueChange = onInputChanged,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.labelLarge.copy(color = Color(0xFF333333)),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopStart
                ) {
                    if (inputText.isEmpty()) {
                        Text(
                            modifier = Modifier
                                .verticalScroll(rememberScrollState()),
                            text = "Type or paste text here...",
                            color = Color(0xFFAAAAAA),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Bottom Row with Paste Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Paste Button
            PasteButton(onClick = onPasteClicked)

            Spacer(modifier = Modifier.weight(1f))

            // Floating Mic Button
            MicButton(
                onClick = onMicClicked
            )
        }
    }
}

@Composable
private fun PasteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(28.dp)
            .background(
                Color(0xFFE1EBFF).copy(alpha = 0.7f),
                RoundedCornerShape(30.dp)
            )
            .border(
                BorderStroke(0.4.dp, Color(0xFF0252FF).copy(0.08f)),
                RoundedCornerShape(30.dp)
            )
            .clickable(
                onClick = onClick
            )
            .padding(horizontal = 10.5.dp, vertical = 6.5.dp, )
            ,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_paste),
            contentDescription = null,
            tint = PrimaryColor,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "Paste",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Medium
            ),
            color = PrimaryColor
        )
    }
}


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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color(0xFFF4F7FC),
                shape = RoundedCornerShape(8.dp)
            )
            .border(
                BorderStroke(1.dp, Color(0xFFEAEAEA)),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp)
    ){
        // Translated Text Display
        Text(
            text = translatedText ?: "Translation will appear here...",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF333333),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(bottom = 8.dp)
                .verticalScroll(rememberScrollState())
        )

        HorizontalDivider(
            modifier = Modifier.height(1.dp),
            color = Color(0xFFE1EBFF)
        )

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
        modifier = modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Action Icons Row
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Speaker Button
             TranslationActionButton(
                icon = painterResource(Res.drawable.ic_volume_outline),
                contentDescription = "Read aloud",
                onClick = onSpeakClicked
            )

            // Copy Button
            TranslationActionButton(
                icon = painterResource(Res.drawable.ic_copy_outline),
                contentDescription = "Copy",
                onClick = onCopyClicked
            )

            // Share Button
            TranslationActionButton(
                icon = painterResource(Res.drawable.ic_share_outline),
                contentDescription = "Share",
                onClick = onShareClicked
            )

            // Favorite Button
            TranslationActionButton(
                icon = painterResource(Res.drawable.ic_star_filled),
                contentDescription = "Save to favorites",
                onClick = onFavoriteClicked
            )
        }

        // Character Counter
        Text(
            text = "$charCount/3000",
            style = MaterialTheme.typography.bodySmall.copy(
                fontSize = 11.sp,
                fontStyle = FontStyle.Italic,
                fontWeight = FontWeight.Medium
            ),
            color = Color(0xFF6A6A6A),
            textAlign = TextAlign.End,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
private fun TranslationActionButton(
    icon: Painter,
    color: Color = PrimaryColor,
    contentDescription: String,
    onClick: () -> Unit,
    size: Dp = 28.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = icon,
            contentDescription = contentDescription,
            tint = color
        )
    }
}

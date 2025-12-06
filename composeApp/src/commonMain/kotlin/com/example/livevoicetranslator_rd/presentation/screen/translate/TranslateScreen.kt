package com.example.livevoicetranslator_rd.presentation.screen.translate


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.domain.model.ModelDownloadState
import org.koin.compose.viewmodel.koinViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

@Composable
fun TranslateScreen(
    viewModel: TranslateViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        OutlinedTextField(
            value = uiState.inputText,
            onValueChange = { viewModel.onInputChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            placeholder = { Text("Type or paste text here...") }
        )

        Spacer(Modifier.height(8.dp))

        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Detected: ${uiState.languageDetectionResult.languageCode ?: "--"}")
                Text("${uiState.charCount} chars")
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Source Confidence: ${uiState.languageDetectionResult.confidence}")
                TargetLanguageDropdown()
            }
        }

        Spacer(Modifier.height(8.dp))

        when (val ms = uiState.modelDownloadState) {
            is ModelDownloadState.Downloading -> {
                /*if (uiState.modelDownloadState is ModelDownloadState.Downloading) {
                    CircularProgressIndicator()
                    Text("Downloading model for ${uiState.detectedLanguage ?: "…"}")
                }*/

                Column {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(4.dp))
                    Text("Downloading model: ${ms.source} → ${ms.target}")
                }
            }

            is ModelDownloadState.Completed -> {
                Text("Model ready: ${ms.source} → ${ms.target}")
            }

            is ModelDownloadState.Failed -> {
                Text("Model download failed: ${ms.reason ?: "unknown"}")
            }

            else -> { /* idle */ }
        }

        Spacer(Modifier.height(12.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (uiState.isLoading) {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                    Spacer(Modifier.height(8.dp))
                }
                Text(
                    text = uiState.translatedText ?: "Translation will appear here",
                    style = MaterialTheme.typography.bodyLarge
                )
                Spacer(Modifier.height(8.dp))

                Row {
                    TextButton(onClick = {
                        viewModel.onCopyClicked()
                    }) {
                        Text("Copy")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = {
                        viewModel.onShareClicked()
                    }) {
                        Text("Share")
                    }
                    Spacer(Modifier.width(8.dp))
                    TextButton(onClick = {
                        viewModel.onSpeakClicked()
                    }) {
                        Text("Speak")
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = {
                        viewModel.manualSaveFavorite()
                    }) {
                        Icon(Icons.Default.Favorite, contentDescription = "Save")
                    }
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        Text("History", style = MaterialTheme.typography.titleMedium)
        Column(modifier = Modifier.fillMaxWidth()) {
            uiState.history.take(8).forEach { item ->
                // later create another data class for showing histories
                Text(
                    "${item.engine} → ${item.translatedText}",
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

@Composable
fun TargetLanguageDropdown(
    viewModel: TranslateViewModel = koinViewModel()
) {
    val uiState = viewModel.uiState.value
    val languages = mlKitSupportedLanguages

    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.wrapContentWidth(),
        contentAlignment = Alignment.Center
    ) {

        Text(
            text = languages[uiState.targetLang] ?: "Select Language",
            modifier = Modifier
                .clickable { expanded = true }
                .padding(6.dp)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { (code, name) ->
                DropdownMenuItem(
                    onClick = {
                        viewModel.onTargetLangChanged(code)
                        expanded = false
                    },
                    text = { Text(name) }
                )
            }
        }
    }
}

val mlKitSupportedLanguages = mapOf(
    "af" to "Afrikaans",
    "ar" to "Arabic",
    "be" to "Belarusian",
    "bg" to "Bulgarian",
    "bn" to "Bengali",
    "ca" to "Catalan",
    "cs" to "Czech",
    "da" to "Danish",
    "de" to "German",
    "el" to "Greek",
    "en" to "English",
    "eo" to "Esperanto",
    "es" to "Spanish",
    "et" to "Estonian",
    "fa" to "Persian",
    "fi" to "Finnish",
    "fr" to "French",
    "ga" to "Irish",
    "gl" to "Galician",
    "gu" to "Gujarati",
    "he" to "Hebrew",
    "hi" to "Hindi",
    "hr" to "Croatian",
    "ht" to "Haitian Creole",
    "hu" to "Hungarian",
    "id" to "Indonesian",
    "is" to "Icelandic",
    "it" to "Italian",
    "ja" to "Japanese",
    "ka" to "Georgian",
    "ko" to "Korean",
    "lt" to "Lithuanian",
    "lv" to "Latvian",
    "mk" to "Macedonian",
    "mr" to "Marathi",
    "ms" to "Malay",
    "mt" to "Maltese",
    "nl" to "Dutch",
    "no" to "Norwegian",
    "pl" to "Polish",
    "pt" to "Portuguese",
    "ro" to "Romanian",
    "ru" to "Russian",
    "sk" to "Slovak",
    "sl" to "Slovenian",
    "sq" to "Albanian",
    "sr" to "Serbian",
    "sv" to "Swedish",
    "sw" to "Swahili",
    "ta" to "Tamil",
    "te" to "Telugu",
    "th" to "Thai",
    "tr" to "Turkish",
    "uk" to "Ukrainian",
    "ur" to "Urdu",
    "vi" to "Vietnamese",
    "zh" to "Chinese"
)



package com.example.livevoicetranslator_rd.presentation.screen.translate


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.kmppractice.presentation.translate.TranslateViewModel
import org.koin.compose.viewmodel.koinViewModel

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

        // Input field
        BasicTextField(
            value = uiState.inputText,
            onValueChange = { viewModel.onInputChanged(it) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Detected language
        Text(
            text = "Detected: ${uiState.detectedLanguage ?: "--"}",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Translate button
        Button(
            onClick = { viewModel.translate() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Translate")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Translation output
        Text(
            text = uiState.translatedText ?: "Translation will appear here",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Save to Favorites
        Button(
            onClick = { viewModel.saveToFavorites() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save to Favorites")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // History display
        Text("History:", style = MaterialTheme.typography.titleMedium)
        Column(modifier = Modifier.fillMaxWidth()) {
            uiState.history.forEach { item ->
                Text("- ${item.original} → ${item.translated}")
            }
        }
    }
}



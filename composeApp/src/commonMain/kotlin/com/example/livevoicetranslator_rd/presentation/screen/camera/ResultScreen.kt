package com.example.livevoicetranslator_rd.presentation.screen.camera

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun ResultScreen(
    initialText: String,
    onBack: () -> Unit,
    onTranslate: (String) -> Unit
) {
    var text by remember { mutableStateOf(initialText) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            // Custom TopBar or just a header
            Text(
                text = "Result",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.fillMaxWidth().height(200.dp),
                label = { Text("Recognized Text") }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onTranslate(text) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Translate")
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Retake")
            }
        }
    }
}

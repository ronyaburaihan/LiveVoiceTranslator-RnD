package com.example.livevoicetranslator_rd.presentation.screen.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.presentation.component.*
import org.koin.compose.viewmodel.koinViewModel


val GoogleBlue = Color(0xFF4285F4)
val GoogleGreen = Color(0xFF34A853)
val LightGrayBg = Color(0xFFF8F9FA)
val GrayText = Color(0xFF757575)
@Composable
fun ConversationScreen() {
    val viewModel: ConversationViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val transcriptState by viewModel.transcriptState.collectAsState()
    
    ConversationScreenContent(
        uiState = uiState,
        transcriptState = transcriptState,
        onLeftMicClick = viewModel::onLeftMicClick,
        onRightMicClick = viewModel::onRightMicClick,
        onLeftMicLongPress = viewModel::onLeftMicLongPress,
        onRightMicLongPress = viewModel::onRightMicLongPress,
        onMicReleased = viewModel::onMicReleased,
        onLeftLanguageSelected = viewModel::onLeftLanguageSelected,
        onRightLanguageSelected = viewModel::onRightLanguageSelected,
        onCopyClick = viewModel::onClickCopy,
        onDismissRequest = viewModel::onDismissRequest,
        openAppSettings = viewModel::openAppSettings,
        onUiEventHandled = viewModel::onUiEventHandled,
        onClearConversation = viewModel::clearConversation
    )
    
    // Handle UI events
    val uiEvent by viewModel.uiEvent.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    
    uiEvent?.let { event ->
        when (event) {
            is ConversationViewModel.UiEvent.ShowSnackbar -> {
                LaunchedEffect(event) {
                    snackbarHostState.showSnackbar(event.message)
                    viewModel.onUiEventHandled()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConversationScreenContent(
    uiState: ConversationViewModel.ConversationUiState,
    transcriptState: com.example.livevoicetranslator_rd.domain.model.speachtotext.TranscriptState,
    onLeftMicClick: () -> Unit,
    onRightMicClick: () -> Unit,
    onLeftMicLongPress: () -> Unit,
    onRightMicLongPress: () -> Unit,
    onMicReleased: () -> Unit,
    onLeftLanguageSelected: (String) -> Unit,
    onRightLanguageSelected: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
    openAppSettings: () -> Unit,
    onUiEventHandled: () -> Unit,
    onClearConversation: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBg)
    ) {
        
//        // Snackbar host
//        SnackbarHost(
//            hostState = snackbarHostState,
//            modifier = Modifier.align(Alignment.TopCenter)
//        )
        // Messages area
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 180.dp)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = true
        ) {
            // Clear button at the top
            if (uiState.messages.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(
                            onClick = onClearConversation,
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.Gray)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ClearAll,
                                contentDescription = "Clear",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Clear", fontSize = 12.sp)
                        }
                    }
                }
            }
            // Show existing messages
            items(uiState.messages.reversed()) { message ->
                TranslationCard(
                    sourceText = message.sourceText,
                    translatedText = message.translatedText,
                    accentColor = if (message.isLeftSide) GoogleBlue else GoogleGreen,
                    isLeftAccent = message.isLeftSide,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            // Empty state
            if (uiState.messages.isEmpty() && transcriptState.listeningStatus == com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus.INACTIVE) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = "No messages",
                                modifier = Modifier.size(64.dp),
                                tint = Color.Gray.copy(alpha = 0.5f)
                            )
                            Text(
                                text = "Tap the microphone to start translating",
                                color = Color.Gray,
                                fontSize = 16.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }
            }
            
            // Show real-time transcription while listening
            if (transcriptState.listeningStatus != com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus.INACTIVE && 
                !transcriptState.transcript.isNullOrEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.7f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = if (uiState.isLeftMicActive) GoogleBlue else GoogleGreen
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = transcriptState.transcript,
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }

        // Bottom controls
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            color = Color.White,
            shadowElevation = 16.dp // Strong shadow for the bottom sheet effect
        ) {
            Row(
                modifier = Modifier
                    .padding(vertical = 24.dp, horizontal = 32.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top
            ) {
                // Left Controls (Blue)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    MicButton(
                        color = if (uiState.isLeftMicActive) GoogleBlue.copy(alpha = 0.8f) else GoogleBlue,
                        onClick = onLeftMicClick,
                        onLongClick = onLeftMicLongPress,
                        onLongClickRelease = onMicReleased
                    )
                    if (uiState.isLeftMicActive) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val statusText = when (transcriptState.listeningStatus) {
                            com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus.LISTENING -> "Listening..."
                            com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus.INACTIVE -> "Processing..."
                            else -> "Ready"
                        }
                        Text(
                            text = statusText,
                            color = GoogleBlue,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LanguageDropdownConversation(
                        selectedLanguage = uiState.leftLanguage,
                        availableLanguages = uiState.availableLanguages,
                        color = GoogleBlue,
                        onLanguageSelected = onLeftLanguageSelected
                    )
                }

                // Right Controls (Green)
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    MicButton(
                        color = if (uiState.isRightMicActive) GoogleGreen.copy(alpha = 0.8f) else GoogleGreen,
                        onClick = onRightMicClick,
                        onLongClick = onRightMicLongPress,
                        onLongClickRelease = onMicReleased
                    )
                    if (uiState.isRightMicActive) {
                        Spacer(modifier = Modifier.height(8.dp))
                        val statusText = when (transcriptState.listeningStatus) {
                            com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus.LISTENING -> "Listening..."
                            com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus.INACTIVE -> "Processing..."
                            else -> "Ready"
                        }
                        Text(
                            text = statusText,
                            color = GoogleGreen,
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    LanguageDropdownConversation(
                        selectedLanguage = uiState.rightLanguage,
                        availableLanguages = uiState.availableLanguages,
                        color = GoogleGreen,
                        onLanguageSelected = onRightLanguageSelected
                    )
                }
            }
        }
        
        // Permission dialog
        if (transcriptState.showPermissionNeedDialog) {
            AlertDialog(
                onDismissRequest = onDismissRequest,
                title = { Text("Permission Required") },
                text = { Text("This app needs microphone permission to use speech recognition. Please grant the permission in app settings.") },
                confirmButton = {
                    TextButton(onClick = openAppSettings) {
                        Text("Open Settings")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissRequest) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
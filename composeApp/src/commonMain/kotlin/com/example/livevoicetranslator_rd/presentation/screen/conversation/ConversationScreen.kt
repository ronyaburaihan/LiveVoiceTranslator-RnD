package com.example.livevoicetranslator_rd.presentation.screen.conversation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.presentation.component.LanguageDropdownConversation
import com.example.livevoicetranslator_rd.presentation.component.MicButton
import com.example.livevoicetranslator_rd.presentation.component.TranslationCard
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_mic_default_conversation
import org.jetbrains.compose.resources.painterResource
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
        liveText = uiState.liveText,
        statusText = uiState.statusText,
        targetLanguageCode = uiState.targetLanguageCode,
        transcriptState = transcriptState,
        onLeftMicClick = viewModel::onLeftMicClick,
        onRightMicClick = viewModel::onRightMicClick,
        onLeftMicLongPress = viewModel::onLeftMicLongPress,
        onRightMicLongPress = viewModel::onRightMicLongPress,
        onLeftLanguageSelected = viewModel::onLeftLanguageSelected,
        onRightLanguageSelected = viewModel::onRightLanguageSelected,
        onCopyClick = viewModel::onClickCopy,
        onDismissRequest = viewModel::onDismissRequest,
        openAppSettings = viewModel::openAppSettings,
        onUiEventHandled = viewModel::onUiEventHandled,
        onClearConversation = viewModel::clearConversation,
        onSpeakClick = viewModel::speak
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
    liveText: String,
    statusText: String,
    targetLanguageCode: String,
    transcriptState: com.example.livevoicetranslator_rd.domain.model.speachtotext.TranscriptState,
    onLeftMicClick: () -> Unit,
    onRightMicClick: () -> Unit,
    onLeftMicLongPress: () -> Unit,
    onRightMicLongPress: () -> Unit,
    onLeftLanguageSelected: (String) -> Unit,
    onRightLanguageSelected: (String) -> Unit,
    onCopyClick: (String) -> Unit,
    onDismissRequest: () -> Unit,
    openAppSettings: () -> Unit,
    onUiEventHandled: () -> Unit,
    onClearConversation: () -> Unit,
    onSpeakClick: (String, String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGrayBg)
    ) {
        // Radial Gradient Background
        val density = androidx.compose.ui.platform.LocalDensity.current
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val centerOffset = androidx.compose.ui.geometry.Offset(
                x = constraints.maxWidth / 2f,
                y = constraints.maxHeight / 2f - with(density) { 52.dp.toPx() } // Shift up to match visual center
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = androidx.compose.ui.graphics.Brush.radialGradient(
                            colors = listOf(Color(0xFF237CCC).copy(alpha = 0.11f), Color.Transparent),
                            radius = with(density) { 240.dp.toPx() },
                            center = centerOffset
                        )
                    )
            )
        }

//        // Snackbar host
//        SnackbarHost(
//            hostState = snackbarHostState,
//            modifier = Modifier.align(Alignment.TopCenter)
//        )
        // Messages area
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 120.dp) // 101dp bar + padding
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

            // Show real-time transcription while listening
            if (transcriptState.listeningStatus != com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus.INACTIVE &&
                !transcriptState.transcript.isNullOrEmpty()
            ) {
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
                                text = liveText,
                                color = Color.Gray,
                                fontSize = 16.sp,
                                fontStyle = FontStyle.Italic
                            )
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
                    onSpeakClick = { onSpeakClick(message.translatedText, targetLanguageCode) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Empty state
            if (uiState.messages.isEmpty() && transcriptState.listeningStatus == com.example.livevoicetranslator_rd.domain.model.speachtotext.ListeningStatus.INACTIVE) {
                item {
                    BoxWithConstraints(
                        modifier = Modifier.fillParentMaxHeight()
                    ) {
                        val availableHeight = maxHeight
                        val iconSize = availableHeight * 0.2563f

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_mic_default_conversation),
                                    contentDescription = "Mic icon.",
                                    modifier = Modifier.size(iconSize),
                                    tint = Color.Unspecified
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "Start your conversation",
                                    color = Color(0xFF333333),
                                    style = MaterialTheme.typography.headlineLarge,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(9.dp))
                                Text(
                                    text = "Tap a microphone below and start speaking.",
                                    color = Color(0xFF777777),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 13.sp
                                    ),
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }


        }

        // Bottom controls
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(101.dp) // 26dp (half mic) + 75dp (bar)
        ) {
            // White Background Bar
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(75.dp),
                color = Color.White,
                shadowElevation = 16.dp,
                content = {}
            )

            // Controls
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Left Controls
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MicButton(
                        color = if (uiState.isLeftMicActive) GoogleBlue.copy(alpha = 0.8f) else GoogleBlue,
                        onClick = { onLeftMicClick() },
                        onLongClick = {},
                        onLongClickRelease = {},
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    LanguageDropdownConversation(
                        backgroundColor = Color(0xFFEFF4FF),
                        selectedLanguage = uiState.leftLanguage,
                        availableLanguages = uiState.availableLanguages,
                        color = Color(0xFF0252FF),
                        onLanguageSelected = onLeftLanguageSelected
                    )
                }

                // Right Controls
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    MicButton(
                        shadowColor = Color(0x4D00B253),
                        color = if (uiState.isRightMicActive) GoogleGreen.copy(alpha = 0.8f) else GoogleGreen,
                        onClick = { onRightMicClick() },
                        onLongClick = {},
                        onLongClickRelease = {},
                        modifier = Modifier.size(52.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    LanguageDropdownConversation(
                        selectedLanguage = uiState.rightLanguage,
                        availableLanguages = uiState.availableLanguages,
                        color = Color(0xFF00B155),
                        backgroundColor = Color(0xFF00B154).copy(0.10f),
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

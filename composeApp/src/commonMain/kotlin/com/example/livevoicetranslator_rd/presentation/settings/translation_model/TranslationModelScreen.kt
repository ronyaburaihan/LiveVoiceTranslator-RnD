package com.example.livevoicetranslator_rd.presentation.settings.translation_model

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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.livevoicetranslator_rd.presentation.app.isPremium
import com.example.livevoicetranslator_rd.presentation.component.AppTopbarWithBack
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.util.LocalAppState
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_translate
import org.jetbrains.compose.resources.painterResource

@Composable
fun TranslationModelScreen(
    viewModel: TranslationModelViewModel = koinViewModel()
) {
    val navController = LocalNavController.current
    val state by viewModel.uiState.collectAsState()
    val appState = LocalAppState.current

    TranslationModelScreenContent(
        isPremium = appState.isPremium,
        onBackClick = navController::navigateUp,
        uiState = state,
        onDownloadClick = viewModel::onDownloadClick
    )

}

@Composable
fun TranslationModelScreenContent(
    isPremium: Boolean = false,
    onBackClick: () -> Unit,
    uiState: TranslationModelUiState,
    onDownloadClick: (String) -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            AppTopbarWithBack(
                title = "Translation Model",
                onBackClick = onBackClick
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(horizontal = dimens.horizontalPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            val items = uiState.items
            items.forEach { item ->
                CustomSettingModelList(
                    title = item.language.label,
                    isChecked = item.isDownloaded,
                    isDownloading = item.isDownloading,
                    onClick = {
                        if (!item.isDownloaded && !item.isDownloading) {
                            onDownloadClick(item.language.code)
                        }
                    }
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

        }
    }
}


@Composable
private fun CustomSettingModelList(
    title: String,
    isChecked: Boolean,
    isDownloading: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(dimens.cornerRadius),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Box(
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 7.dp)
                        .size(38.dp)
                        .background(
                            color = Color.Blue.copy(0.04f),
                            shape = RoundedCornerShape(4.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_translate),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )


            }

            if (isChecked) {
                Icon(
                    painter = painterResource(Res.drawable.ic_translate),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 40.dp)
                        .size(31.dp),
                    tint = Color.Unspecified
                )
            } else {
                Box(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onClick() }
                        .padding(horizontal = 14.dp, vertical = 4.dp),
                ) {
                    Text(
                        text = if (isDownloading) "Downloading" else "Download",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

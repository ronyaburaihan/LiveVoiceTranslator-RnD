package com.example.livevoicetranslator_rd.presentation.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.livevoicetranslator_rd.presentation.component.AppTopBar
import com.example.livevoicetranslator_rd.presentation.component.AppTopBarTitle
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.theme.dividerColor
import com.example.livevoicetranslator_rd.presentation.theme.textColour
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_app_language
import livevoicetranslatorrd.composeapp.generated.resources.ic_auto_save_history
import livevoicetranslatorrd.composeapp.generated.resources.ic_history
import livevoicetranslatorrd.composeapp.generated.resources.ic_premium
import livevoicetranslatorrd.composeapp.generated.resources.ic_read_translation
import livevoicetranslatorrd.composeapp.generated.resources.ic_translate
import livevoicetranslatorrd.composeapp.generated.resources.ic_translate_engine
import livevoicetranslatorrd.composeapp.generated.resources.ic_voice_setting
import livevoicetranslatorrd.composeapp.generated.resources.setting_sub_title_app_language
import livevoicetranslatorrd.composeapp.generated.resources.setting_sub_title_translate_engine
import livevoicetranslatorrd.composeapp.generated.resources.setting_title_app_language
import livevoicetranslatorrd.composeapp.generated.resources.setting_title_auto_save_history
import livevoicetranslatorrd.composeapp.generated.resources.setting_title_automatically_read_translation
import livevoicetranslatorrd.composeapp.generated.resources.setting_title_clear_history
import livevoicetranslatorrd.composeapp.generated.resources.setting_title_manage_subscription
import livevoicetranslatorrd.composeapp.generated.resources.setting_title_translate_engine
import livevoicetranslatorrd.composeapp.generated.resources.setting_title_voice_settings
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SettingScreen(title: String) {
    val navController = LocalNavController.current
    var autoReadTranslation by remember { mutableStateOf(false) }
    var autoSaveHistory by remember { mutableStateOf(true) }

    SettingsScreenContent(
        navController = navController,
        title = title,
        autoReadTranslation = autoReadTranslation,
        onAutoReadTranslationChange = { autoReadTranslation = it },
        autoSaveHistory = autoSaveHistory,
        onAutoSaveHistoryChange = { autoSaveHistory = it },
        onBackClick = { navController.navigateUp() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreenContent(
    navController: NavController,
    title: String,
    autoReadTranslation: Boolean,
    onAutoReadTranslationChange: (Boolean) -> Unit,
    autoSaveHistory: Boolean,
    onAutoSaveHistoryChange: (Boolean) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            AppTopBar(
                title = { AppTopBarTitle(title = title) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            modifier = Modifier.size(dimens.iconSize),
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface
                        )
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            SettingsNavigationItem(
                icon = Res.drawable.ic_app_language,
                title = "App Language",
                subtitle = "English",
                onClick = {}
            )

            SettingsNavigationItem(
                icon = Res.drawable.ic_translate_engine,
                title = "Translate Engine",
                subtitle = "AI Model",
                onClick = {}
            )

            SettingsNavigationItem(
                icon = Res.drawable.ic_voice_setting,
                title = "Voice Settings",
                subtitle = null,
                onClick = {}
            )

            SettingsSwitchItem(
                icon = Res.drawable.ic_read_translation,
                title = "Automatically Read Translation",
                checked = autoReadTranslation,
                onCheckedChange = onAutoReadTranslationChange
            )

            SettingsSwitchItem(
                icon = Res.drawable.ic_auto_save_history,
                title = "Auto Save History",
                checked = autoSaveHistory,
                onCheckedChange = onAutoSaveHistoryChange
            )

            SettingsNavigationItem(
                icon = Res.drawable.ic_premium,
                title = "Manage Subscription",
                subtitle = null,
                onClick = {}
            )

            SettingsNavigationItem(
                icon = Res.drawable.ic_history,
                title = "Clear History",
                subtitle = null,
                onClick = {}
            )

            SettingsNavigationItem(
                icon = Res.drawable.ic_translate,
                title = "Translation Model",
                subtitle = null,
                onClick = {
                    navController.navigate(ScreenRoute.TranslationModel)
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "ID: 32165    App Version 1.0",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
        }
    }
}


@Composable
fun SettingsNavigationItem(
    icon: DrawableResource,
    title: String,
    subtitle: String?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = textColour
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Box(modifier = Modifier.size(32.dp).clickable(onClick = onClick)) {
                Icon(
                    modifier = Modifier.align(Alignment.Center),
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = DividerDefaults.Thickness,
            color = dividerColor
        )
    }
}

@Composable
fun SettingsSwitchItem(
    icon: DrawableResource,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = textColour,
                modifier = Modifier.weight(1f)
            )

            val interactionSource = remember { MutableInteractionSource() }

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier
                    .scale(0.7f)
                    .indication(interactionSource, null),
                interactionSource = interactionSource,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.surface,
                    checkedTrackColor = PrimaryColor,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onBackground,
                    uncheckedTrackColor = MaterialTheme.colorScheme.onSurface
                )
            )


        }
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = DividerDefaults.Thickness,
            color = dividerColor
        )
    }
}


@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    MaterialTheme {
        SettingScreen(
            title = ""
        )
    }
}
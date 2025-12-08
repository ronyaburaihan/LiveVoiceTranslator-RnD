package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryBrush
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.app_name
import org.jetbrains.compose.resources.stringResource

@Composable
fun AppTopBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    windowInsets: WindowInsets = WindowInsets.systemBars
        .only(WindowInsetsSides.Horizontal),
    containerColor: Color = MaterialTheme.colorScheme.surface,
    contentColor: Color = MaterialTheme.colorScheme.onSurface,
    contentAlignment: Alignment = Alignment.CenterStart
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = Modifier
                .windowInsetsPadding(windowInsets)
                .height(dimens.topBarHeight)
                .background(PrimaryBrush)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Navigation Icon
            Box(
                modifier = Modifier.wrapContentSize(),
                contentAlignment = Alignment.Center
            ) {
                navigationIcon()
            }

            // Title
            Box(
                modifier = Modifier
                    .weight(1f),
                contentAlignment = contentAlignment
            ) {
                ProvideTextStyle(
                    value = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.surface
                    )
                ) {
                    title()
                }
            }

            // Actions
            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                Row(
                    modifier = Modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    content = actions
                )
            }
        }
    }
}

@Composable
fun AppTopbarWithBack(
    title: String = stringResource(Res.string.app_name),
    onBackClick: () -> Unit = {}
) {
    AppTopBar(
        modifier = Modifier,
        title = {
            AppTopBarTitle(
                title = title
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                content = {
                    Icon(
                        modifier = Modifier
                            .size(dimens.iconSize)
                            .padding(2.dp),
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.surface
                    )
                }
            )
        }
    )
}

@Composable
fun AppTopBarTitle(
    title: String = stringResource(Res.string.app_name)
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.surface
        ),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

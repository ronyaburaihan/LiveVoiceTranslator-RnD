package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import com.example.livevoicetranslator_rd.presentation.theme.PremiumGradient
import com.example.livevoicetranslator_rd.presentation.theme.PremiumIconColor
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryBrush
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.app_name
import livevoicetranslatorrd.composeapp.generated.resources.ic_crown
import livevoicetranslatorrd.composeapp.generated.resources.ic_translate
import livevoicetranslatorrd.composeapp.generated.resources.pro
import livevoicetranslatorrd.composeapp.generated.resources.translate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AppDrawer(
    drawerState: DrawerState,
    scope: CoroutineScope,
    isPremium: Boolean,
) {
    val navController = LocalNavController.current
    ModalDrawerSheet(
        modifier = Modifier.fillMaxHeight(),
        drawerContainerColor = Color.Transparent,
        windowInsets = WindowInsets(0),
        drawerShape = RectangleShape
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .widthIn(max = dimens.drawerMinWidth)
                .verticalScroll(rememberScrollState())
                .background(MaterialTheme.colorScheme.background),
            horizontalAlignment = Alignment.Start
        ) {
            Box(
                modifier = Modifier
                    .background(brush = PrimaryBrush)
                    .fillMaxWidth()
                    .padding(top = 44.dp, bottom = 17.dp)

            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 16.dp)
                ) {
                    Text(
                        text = stringResource(Res.string.app_name),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.background
                    )
                    Text(
                        text = stringResource(Res.string.app_name),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.background,
                        modifier = Modifier.padding(top = 1.dp)
                    )
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.padding(top = 24.dp, bottom = 16.dp)) {
                    DrawerItem(
                        icon = Res.drawable.ic_crown,
                        label = stringResource(Res.string.pro),
                        backgroundGradient = PremiumGradient,
                        fontWeight = FontWeight.SemiBold,
                        iconSize = DpSize(18.dp, 18.dp),
                        textColor = PremiumIconColor,
                        iconTint = PremiumIconColor

                    ) {
                        scope.launch { drawerState.close() }
                        navController.navigate(ScreenRoute.Premium) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
                DrawerItem(
                    label = stringResource(Res.string.translate),
                    icon = Res.drawable.ic_translate,
                    iconTint = Color.Unspecified,
                ) {
                    scope.launch { drawerState.close() }
                }

            }
        }
    }
}

@Composable
fun DrawerItem(
    icon: DrawableResource? = null,
    iconSize: DpSize = DpSize(20.dp, 20.dp),
    label: String,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    backgroundGradient: Brush? = null,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    iconTint: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    fontWeight: FontWeight = FontWeight.Medium,
    onClick: () -> Unit

) {
    Row(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .then(
                if (backgroundGradient != null) {
                    Modifier.background(backgroundGradient)
                } else {
                    Modifier.background(backgroundColor)
                }
            )
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        icon?.let {
            Icon(
                painter = painterResource(icon),
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(iconSize)
            )

            Spacer(modifier = Modifier.width(12.dp))
        }

        Text(
            text = label,
            style = style,
            textAlign = TextAlign.Left,
            color = textColor,
            fontWeight = fontWeight
        )
        Spacer(modifier = Modifier.weight(0.8f))
    }
}


@Preview(showBackground = true)
@Composable
fun DrawerItemPreview() {
    DrawerItem(
        icon = Res.drawable.ic_crown,
        label = "Become a Pro",
        backgroundColor = Color(0xFFFFC107),
        textColor = Color.Black,
        iconTint = Color.Black,
        onClick = {}
    )
}

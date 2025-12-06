package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
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
import com.example.livevoicetranslator_rd.presentation.theme.BackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.OnBackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.PremiumButtonColor
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import com.example.livevoicetranslator_rd.presentation.theme.PremiumGradient
import com.example.livevoicetranslator_rd.presentation.theme.PremiumIconColor
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryBrush
import com.example.livevoicetranslator_rd.presentation.theme.SurfaceColor
import com.example.livevoicetranslator_rd.presentation.theme.black
import com.example.livevoicetranslator_rd.presentation.theme.buttonBackGround
import com.example.livevoicetranslator_rd.presentation.theme.dividerColor
import com.example.livevoicetranslator_rd.presentation.theme.textColour
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.app_name
import livevoicetranslatorrd.composeapp.generated.resources.clear_conversation
import livevoicetranslatorrd.composeapp.generated.resources.contact_us
import livevoicetranslatorrd.composeapp.generated.resources.ic_coin
import livevoicetranslatorrd.composeapp.generated.resources.ic_contact_us
import livevoicetranslatorrd.composeapp.generated.resources.ic_crown
import livevoicetranslatorrd.composeapp.generated.resources.ic_delete
import livevoicetranslatorrd.composeapp.generated.resources.ic_history
import livevoicetranslatorrd.composeapp.generated.resources.ic_offline_translation
import livevoicetranslatorrd.composeapp.generated.resources.ic_rating_star
import livevoicetranslatorrd.composeapp.generated.resources.ic_setting
import livevoicetranslatorrd.composeapp.generated.resources.ic_share_app
import livevoicetranslatorrd.composeapp.generated.resources.ic_translate
import livevoicetranslatorrd.composeapp.generated.resources.offline_translation
import livevoicetranslatorrd.composeapp.generated.resources.pro
import livevoicetranslatorrd.composeapp.generated.resources.rate_app
import livevoicetranslatorrd.composeapp.generated.resources.refer_get_credit
import livevoicetranslatorrd.composeapp.generated.resources.settings
import livevoicetranslatorrd.composeapp.generated.resources.share_app
import livevoicetranslatorrd.composeapp.generated.resources.translate
import livevoicetranslatorrd.composeapp.generated.resources.translate_history
import livevoicetranslatorrd.composeapp.generated.resources.unlock_premium
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
    val settingsTitle = stringResource(Res.string.settings)
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
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                    DrawerItem(
                        icon = Res.drawable.ic_crown,
                        label = stringResource(Res.string.unlock_premium),
                        backgroundColor = black,
                        fontWeight = FontWeight.SemiBold,
                        iconSize = DpSize(18.dp, 18.dp),
                        textColor = BackgroundColor,
                        iconTint = PremiumButtonColor

                    ) {
                        scope.launch { drawerState.close() }
                        navController.navigate(ScreenRoute.Premium) {
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                DrawerItem(
                    icon = Res.drawable.ic_delete,
                    label = stringResource(Res.string.clear_conversation),
                    backgroundColor = buttonBackGround,
                    fontWeight = FontWeight.SemiBold,
                    iconSize = DpSize(18.dp, 18.dp),
                    textColor = textColour,
                    iconTint = Color.Unspecified

                ) {
                    scope.launch { drawerState.close() }
                }
                DrawerItem(
                    icon = Res.drawable.ic_history,
                    label = stringResource(Res.string.translate_history),
                    backgroundColor = buttonBackGround,
                    fontWeight = FontWeight.SemiBold,
                    iconSize = DpSize(18.dp, 18.dp),
                    textColor = textColour,
                    iconTint = Color.Unspecified

                ) {
                    scope.launch { drawerState.close() }
                }
                DrawerItem(
                    icon = Res.drawable.ic_setting,
                    label = stringResource(Res.string.settings),
                    backgroundColor = buttonBackGround,
                    fontWeight = FontWeight.SemiBold,
                    iconSize = DpSize(18.dp, 18.dp),
                    textColor = textColour,
                    iconTint = Color.Unspecified
                ) {
                    scope.launch {
                        drawerState.close()
                        navController.navigate(
                            ScreenRoute.Settings(title = settingsTitle)
                        )
                    }
                }

                DrawerItem(
                    icon = Res.drawable.ic_offline_translation,
                    label = stringResource(Res.string.offline_translation),
                    backgroundColor = buttonBackGround,
                    fontWeight = FontWeight.SemiBold,
                    iconSize = DpSize(18.dp, 18.dp),
                    textColor = textColour,
                    iconTint = Color.Unspecified

                ) {
                    scope.launch { drawerState.close() }
                }
                DrawerItem(
                    icon = Res.drawable.ic_coin,
                    label = stringResource(Res.string.refer_get_credit),
                    backgroundColor = buttonBackGround,
                    fontWeight = FontWeight.SemiBold,
                    iconSize = DpSize(18.dp, 18.dp),
                    textColor = textColour,
                    iconTint = Color.Unspecified

                ) {
                    scope.launch { drawerState.close() }
                    navController.navigate(ScreenRoute.Referral)
                }
                HorizontalDivider(thickness = 1.dp, color = dividerColor)
                DrawerItem(
                    icon = Res.drawable.ic_share_app,
                    label = stringResource(Res.string.share_app),
                    backgroundColor = buttonBackGround,
                    fontWeight = FontWeight.SemiBold,
                    iconSize = DpSize(18.dp, 18.dp),
                    textColor = textColour,
                    iconTint = Color.Unspecified

                ) {
                    scope.launch { drawerState.close() }
                }
                DrawerItem(
                    icon = Res.drawable.ic_rating_star,
                    label = stringResource(Res.string.rate_app),
                    backgroundColor = buttonBackGround,
                    fontWeight = FontWeight.SemiBold,
                    iconSize = DpSize(18.dp, 18.dp),
                    textColor = textColour,
                    iconTint = Color.Unspecified

                ) {
                    scope.launch { drawerState.close() }
                }
                DrawerItem(
                    icon = Res.drawable.ic_contact_us,
                    label = stringResource(Res.string.contact_us),
                    backgroundColor = buttonBackGround,
                    fontWeight = FontWeight.SemiBold,
                    iconSize = DpSize(18.dp, 18.dp),
                    textColor = textColour,
                    iconTint = Color.Unspecified

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

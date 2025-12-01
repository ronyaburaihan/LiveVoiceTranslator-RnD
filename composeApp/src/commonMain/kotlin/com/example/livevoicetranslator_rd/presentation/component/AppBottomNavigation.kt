package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.theme.dimens
import com.example.livevoicetranslator_rd.presentation.util.LocalAppState
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryBrush
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.camera
import livevoicetranslatorrd.composeapp.generated.resources.conservation
import livevoicetranslatorrd.composeapp.generated.resources.ic_translate
import livevoicetranslatorrd.composeapp.generated.resources.phrases
import livevoicetranslatorrd.composeapp.generated.resources.translate
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.roundToInt


@Composable
fun AppBottomNavigation(
    navController: NavHostController,
    containerColor: Color = MaterialTheme.colorScheme.background,
    selectedBrush: Brush = PrimaryBrush,
    unSelectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    val appState = LocalAppState.current
    val items = listOf(
        AppBottomNavItem.Conservation,
        AppBottomNavItem.Translate,
        AppBottomNavItem.Camera,
        AppBottomNavItem.Phrases,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var indicatorX by remember { mutableFloatStateOf(0f) }
    var indicatorWidth by remember { mutableFloatStateOf(0f) }
    var containerWidth by remember { mutableFloatStateOf(0f) }

    val animatedOffsetX by animateFloatAsState(
        targetValue = indicatorX,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val animatedWidth by animateFloatAsState(
        targetValue = indicatorWidth,
        animationSpec = spring(stiffness = Spring.StiffnessLow)
    )

    val density = LocalDensity.current
    val layoutDirection = LocalLayoutDirection.current
    val isRtl = layoutDirection == LayoutDirection.Rtl

    Surface(
        color = containerColor,
        shadowElevation = dimens.shadowElevation
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(
                Modifier
                    .offset {
                        val offsetX = if (isRtl && containerWidth > 0f) {
                            (containerWidth - animatedOffsetX - animatedWidth).roundToInt()
                        } else {
                            animatedOffsetX.roundToInt()
                        }
                        IntOffset(offsetX, 0)
                    }
                    .width(with(density) { animatedWidth.toDp() })
                    .padding(horizontal = dimens.horizontalPadding)
                    .height(4.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 2.dp,
                            bottomEnd = 2.dp
                        )
                    )
                    .background(selectedBrush)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items.forEach { item ->
                    val isSelected = currentRoute == item.route::class.qualifiedName
                    AppBottomNavigationItem(
                        modifier = Modifier.weight(1f),
                        isSelected = isSelected,
                        item = item,
                        unSelectedContentColor = unSelectedContentColor,
                        selectedBrush = selectedBrush,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        onPositioned = { position, width ->
                            if (currentRoute == item.route::class.qualifiedName) {
                                indicatorX = position.x
                                indicatorWidth = width
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AppBottomNavigationItem(
    modifier: Modifier = Modifier,
    item: AppBottomNavItem,
    isSelected: Boolean,
    unSelectedContentColor: Color,
    selectedBrush: Brush = PrimaryBrush,
    onClick: () -> Unit,
    onPositioned: (Offset, Float) -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.10f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    val textStyle = if (isSelected) MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.SemiBold,
        brush = selectedBrush
    ) else MaterialTheme.typography.bodySmall.copy(
        fontWeight = FontWeight.Normal,
        color = unSelectedContentColor
    )

    Column(
        modifier = modifier
            .clickable { onClick() }
            .onGloballyPositioned {
                val position = it.positionInRoot()
                val width = it.size.width.toFloat()
                onPositioned(position, width)
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(if (isSelected) item.selectedIcon else item.unselectedIcon),
            contentDescription = stringResource(item.label),
            modifier = Modifier.size(dimens.smallIconSize)
                .graphicsLayer(scaleX = scale, scaleY = scale)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(item.label),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = textStyle
        )
    }
}

sealed class AppBottomNavItem(
    val label: StringResource,
    val route: ScreenRoute,
    val selectedIcon: DrawableResource,
    val unselectedIcon: DrawableResource,
) {
    object Conservation : AppBottomNavItem(
        label = Res.string.conservation,
        route = ScreenRoute.Conservation,
        selectedIcon = Res.drawable.ic_translate,
        unselectedIcon = Res.drawable.ic_translate,
    )

    object Translate : AppBottomNavItem(
        label = Res.string.translate,
        route = ScreenRoute.Translate,
        selectedIcon = Res.drawable.ic_translate,
        unselectedIcon = Res.drawable.ic_translate,
    )

    object Camera : AppBottomNavItem(
        label = Res.string.camera,
        route = ScreenRoute.Camera,
        selectedIcon = Res.drawable.ic_translate,
        unselectedIcon = Res.drawable.ic_translate,
    )

    object Phrases : AppBottomNavItem(
        label = Res.string.phrases,
        route = ScreenRoute.Phrases,
        selectedIcon = Res.drawable.ic_translate,
        unselectedIcon = Res.drawable.ic_translate,
    )
}
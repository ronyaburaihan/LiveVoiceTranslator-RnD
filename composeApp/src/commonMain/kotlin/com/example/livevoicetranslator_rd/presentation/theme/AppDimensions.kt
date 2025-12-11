package com.example.livevoicetranslator_rd.presentation.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class AppDimensions(
    val circularIconSize: Dp = 16.dp,
    val iconSize: Dp = 24.dp,
    val smallIconSize: Dp = 22.dp,
    val mediumIconSize: Dp = 23.dp,
    val vectorImageSize: Dp = 28.dp,

    val smallIconSizeConversation: Dp = 16.dp,

    val buttonHeight: Dp = 48.dp,
    val buttonHeightMedium: Dp = 36.dp,
    val buttonHeightLarge: Dp = 70.dp,
    val buttonHeightSmall: Dp = 28.dp,

    val micButtonSize: Dp = 52.dp,

    val bottomBarHeight: Dp = 55.dp,
    val topBarHeight: Dp = 55.dp,
    val filterButtonSize: Dp = 40.dp,
    val cardButtonHeight: Dp = 80.dp,
    val cardButtonCornerRadius: Dp = 12.dp,
    val cardPadding: Dp = 10.dp,

    val conversationBottomContainerHeight: Dp = 101.dp,
    val conversationBottomSurfaceHeight: Dp = 75.dp,

    val spaceBetweenSmall: Dp = 12.dp,
    val spaceBetween: Dp = 16.dp,

    val smallSpacing: Dp = 4.dp,
    val tinySpacing: Dp = 5.dp,
    val smallSpacingMedium: Dp = 9.dp,
    val conversationBottomSpacerHeight: Dp = 120.dp,

    val horizontalPadding: Dp = 20.dp,
    val verticalPadding: Dp = 20.dp,
    val verticalPaddingMedium: Dp = 12.dp,
    val verticalPaddingSmall: Dp = 8.dp,

    val screenHorizontalPaddingSmall: Dp = 16.dp,
    val screenPaddingSmall: Dp = 16.dp,
    val screenPaddingLarge: Dp = 32.dp,

    val contentPadding: PaddingValues = PaddingValues(
        horizontal = horizontalPadding,
        vertical = verticalPadding
    ),

    val cornerRadiusLarge: Dp = 30.dp,
    val cornerRadius: Dp = 16.dp,
    val cornerRadiusMedium: Dp = 12.dp,
    val cornerRadiusSmall: Dp = 8.dp,
    val cornerRadiusDropdown: Dp = 10.dp,
    val cornerRadiusBottomSheet: Dp = 32.dp,
    val cornerRadiusOutlineButton: Dp = 10.dp,

    val borderWidth: Dp = 1.dp,
    val borderWidthHalf: Dp = 0.5.dp,

    val shadowElevation: Dp = 8.dp,
    val smallElevation: Dp = 2.dp,

    val shadowElevationLarge: Dp = 16.dp,

    val labelPadding: Dp = 10.dp,
    val labelTextSize: Dp = 14.dp,
    val lineHeight: TextUnit = 16.sp,
    val onboardingTitleSize: TextUnit = 24.sp,

    val contentPaddingDropdown: PaddingValues = PaddingValues(
        horizontal = 14.dp,
        vertical = 10.dp
    ),

    val drawerMinWidth: Dp = 275.dp,

    val progressStrokeWidth: Dp = 2.dp
)

@Composable
fun rememberAppDimensions(): AppDimensions = remember { AppDimensions() }

val dimens: AppDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current

val LocalDimensions =
    staticCompositionLocalOf<AppDimensions> { error("AppDimensions must be provided") }
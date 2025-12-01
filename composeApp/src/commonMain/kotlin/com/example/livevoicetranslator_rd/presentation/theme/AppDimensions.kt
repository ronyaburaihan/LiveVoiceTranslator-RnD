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
    val iconSize: Dp = 24.dp,
    val smallIconSize: Dp = 22.dp,
    val mediumIconSize: Dp = 23.dp,
    val vectorImageSize: Dp = 28.dp,
    val bottomBarHeight: Dp = 55.dp,
    val horizontalPadding: Dp = 20.dp,
    val verticalPadding: Dp = 20.dp,
    val spaceBetween: Dp = 16.dp,
    val inputHeight: Dp = 54.dp,
    val buttonHeight: Dp = 48.dp,
    val buttonHeightMedium: Dp = 36.dp,
    val buttonHeightLarge: Dp = 70.dp,
    val buttonHeightSmall: Dp = 28.dp,
    val cornerRadiusLarge: Dp = 30.dp,
    val cornerRadius: Dp = 16.dp,
    val cornerRadiusSmall: Dp = 8.dp,
    val cornerRadiusDropdown: Dp = 10.dp,
    val cornerRadiusBottomSheet: Dp = 32.dp,
    val cornerRadiusOutlineButton: Dp=10.dp,
    val filterButtonSize : Dp = 40.dp,
    val cardPadding: Dp = 10.dp,
    val drawerMinWidth: Dp = 275.dp,
    val borderWidth: Dp = 1.dp,
    val borderWidthHalf: Dp = 0.5.dp,
    val topBarHeight: Dp = 55.dp,
    val cardButtonHeight: Dp = 80.dp,
    val cardButtonCornerRadius: Dp = 12.dp,
    val shadowElevation: Dp = 8.dp,
    val labelPadding: Dp = 10.dp,
    val labelTextSize: Dp = 14.dp,
    val lineHeight: TextUnit = 16.sp,
    val onboardingTitleSize: TextUnit = 24.sp,
    val contentPadding: PaddingValues = PaddingValues(
        horizontal = horizontalPadding,
        vertical = verticalPadding
    ),
    val contentPaddingDropdown: PaddingValues = PaddingValues(
        horizontal = 14.dp,
        vertical = 10.dp
    )
)

@Composable
fun rememberAppDimensions(): AppDimensions = remember { AppDimensions() }

val dimens: AppDimensions
    @Composable
    @ReadOnlyComposable
    get() = LocalDimensions.current

val LocalDimensions =
    staticCompositionLocalOf<AppDimensions> { error("AppDimensions must be provided") }
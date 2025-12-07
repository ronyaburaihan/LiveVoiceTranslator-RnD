package com.example.livevoicetranslator_rd.presentation.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val PrimaryColor = Color(0xFF0252FF)
val SecondaryColor = Color(0xFF1434BF)
val ColorTertiary = Color(0xFF00B154)
val BackgroundColor = Color(0xFFFFFFFF)
val OnBackgroundColor = Color(0xFF333333)
val SurfaceColor = Color(0xFFFFFFFC)
val SurfaceContainer = Color(0xFFF8F8F8)
val OnSurfaceColor = Color(0xFF555555)
val OutlineColor = Color(0xFFEEEEEE)
val PremiumButtonColor = Color(0xFFFFC42D)
val PremiumIconColor = Color(0xFF212121)

val PrimaryBrush = Brush.linearGradient(
    colors = listOf(SecondaryColor, PrimaryColor)
)

val HorizontalBrush = Brush.horizontalGradient(
    colors = listOf(SecondaryColor, PrimaryColor),
)

val OutlineBrush = Brush.linearGradient(
    colors = listOf(
        OutlineColor,
        OutlineColor
    )
)

val ContentBorderColor = Color(0xFFEEEEEE)

val CardButtonGrayGradient = Brush.horizontalGradient(
    colors = listOf(
        Color(0xFFBBBBBB),
        Color(0xFFBBBBBB)
    )
)

val CardColorHashtagScreen = Brush.linearGradient(
    colors = listOf(
        Color(0xFFFFFFFF), // light pink tint
        Color.White
    ),
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f) // diagonal fade
)
val IconButtonBorderColor = Brush.linearGradient(
    colors = listOf(
        Color(0xFFEEEEEE),
        Color(0xFFEEEEEE)
    ),
)
val IconButtonColorBlack = Brush.linearGradient(
    colors = listOf(
        Color(0xFF0333333),
        Color(0xFF333333)
    ),
)

val headerBrush = Brush.linearGradient(
    listOf(
        Color(0xFF0252FF),
        Color(0xFF081548)
    )
)



val TransparentColor = Color(0x00000000)
val PurpleColor = Color(0xFF6200EE)
val BlueColor = Color(0xFF2196F3)
val GreenColor = Color(0xFF4CAF50)
val YellowColor = Color(0xFFFFEB3B)
val RedColor = Color(0xFFF44336)
val SeekbarBrash = Brush.linearGradient(
    colors = listOf(Color(0xFFff5a6a), Color(0xFFf914ae)),
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)

val PremiumGradient = Brush.radialGradient(
    colors = listOf(
        Color(0xFFFFE251),
        Color(0xFFFFDD75)
    )
)
val SeekbarBrashUnfill = Brush.linearGradient(
    colors = listOf(Color(0xFFf2f2f2), Color(0xFFf2f2f2)),
    start = Offset(0f, 0f),
    end = Offset(1000f, 1000f)
)


val borderColor = Color(0xFFEAEAEA)
val boxColor = Color(0xFF2C6CFF)
val iconBackgroundColor = Color(0x1A0252FF)
val premiumBackgroundColor = Color(0xFFFFC107)
val textColour = Color(0xFF212121)
val primaryPink = Color(0xFFBF35E9)
val SecondaryPink = Color(0xFF7D02FF)
val secondaryBox = Color(0xFFF3F9FE)
val greenBox = Color(0x0F01B056)
val purpleBox = Color(0x0A7C02FF)
val black = Color(0xFF000000)
val buttonBackGround = Color(0xFFF7F7F7)
val dividerColor = Color(0xFFF1F1F1)
val featureBackground = Color(0xFFFAFAFA)
val yearlyBackground = Color(0xFFFEFDE2)
val monthlyBackground = Color(0xFFE2F2FE)
val defaultCardBorder = Color(0xFFEEEEEE)
val billingDescriptionColor = Color(0xFFA7A7A7)
val billingDescriptionColor2 = Color(0xFFBDBDBD)
val premiumTextColor = Color(0xFFFFE120)



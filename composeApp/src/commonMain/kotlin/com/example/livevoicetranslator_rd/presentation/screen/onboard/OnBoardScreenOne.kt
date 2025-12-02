package com.example.livevoicetranslator_rd.presentation.screen.onboard

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.presentation.component.PrimaryButton
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.get_started
import livevoicetranslatorrd.composeapp.generated.resources.ic_globe
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Data class for nodes
data class DesignNode(
    val code: String,
    val color: Color,
    val angle: Float,
    val radius: Float
)

@Composable
fun OnBoardingScreenOne() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
            .padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Animated orbital system
        OrbitalSystem(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        // Title and description
        TitleSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Call to action button
        PrimaryButton(
            label = stringResource(Res.string.get_started),
            onClick = { /* action */ }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun OrbitalSystem(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbital")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    val languages = remember {
        listOf(
            DesignNode("ES", Color(0xFFBF35E9), 45f, 150f),
            DesignNode("GB", Color(0xFFFFDA0A), 225f, 150f)
        )
    }

    Box(
        modifier = modifier.background(color = Color(0xFFF3F9FE)),
        contentAlignment = Alignment.Center
    ) {
        // Orbital rings
        OrbitalRings(ringCount = 4, maxRadius = 150.dp)

        // Center globe
        CenterGlobe()

        // Orbiting language nodes
        languages.forEach { lang ->
            OrbitingNode(
                language = lang,
                rotation = rotation
            )
        }

        // Small dots on orbits
        SmallOrbitDots(rotation = rotation)
    }
}

@Composable
fun OrbitalRings(
    ringCount: Int,
    maxRadius: Dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(maxRadius)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        repeat(ringCount) { index ->
            val radius = (maxRadius.toPx() / ringCount) * (index + 1)
            drawCircle(
                color = Color(0xFF1C78F2),
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1f)
            )
        }
    }
}

@Composable
fun CenterGlobe(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(64.dp)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF4285F4),
                        Color(0xFF1565C0)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_globe),
            contentDescription = null,
            tint = Color.Unspecified
        )
    }
}

@Composable
fun OrbitingNode(
    language: DesignNode,
    rotation: Float,
    modifier: Modifier = Modifier
) {
    val angleRad = (language.angle + rotation * 0.22).toDouble() * PI / 180.0
    val offsetX = (language.radius * cos(angleRad)).dp
    val offsetY = (language.radius * sin(angleRad)).dp

    Box(
        modifier = modifier.offset(x = offsetX, y = offsetY)
    ) {
        CircleBadge(
            code = language.code,
            color = language.color
        )
    }
}

@Composable
fun CircleBadge(
    code: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = code,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun SmallOrbitDots(
    rotation: Float,
    modifier: Modifier = Modifier
) {
    val dots = remember {
        listOf(
            DesignNode("", Color(0xFF4285F4), 135f, 75f),
            DesignNode("", Color(0xFF4285F4), 320f, 112f),
            DesignNode("", Color(0xFF4285F4), 95f, 150f)
        )
    }

    dots.forEach { dot ->
        val angleRad = (dot.angle + rotation * 0.20).toDouble() * PI / 180.0
        val offsetX = (dot.radius * cos(angleRad)).dp
        val offsetY = (dot.radius * sin(angleRad)).dp

        Box(
            modifier = modifier
                .offset(x = offsetX, y = offsetY)
                .size(6.dp)
                .clip(CircleShape)
                .background(Color(0xFF4285F4))
        )
    }
}

@Composable
fun TitleSection(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Progress indicator
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(32.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFF4285F4))
            )
            Box(
                modifier = Modifier
                    .width(8.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(Color(0xFFE3E8F8))
            )
        }

        Text(
            text = buildAnnotatedString {
                append("Break ")
                withStyle(SpanStyle(color = Color(0xFF4285F4))) {
                    append("Language")
                }
                append(" Barriers\nInstantly")
            },
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Translate voice, text, and camera in real\ntime across 200+ languages.",
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF666666),
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewOnBoardingScreenOne() {
    MaterialTheme {
        OnBoardingScreenOne()
    }
}
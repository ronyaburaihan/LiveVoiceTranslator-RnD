package com.example.livevoicetranslator_rd.presentation.screen.onboard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.livevoicetranslator_rd.presentation.component.PrimaryButton
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.theme.BackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.GreenColor
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import com.example.livevoicetranslator_rd.presentation.theme.SecondaryColor
import com.example.livevoicetranslator_rd.presentation.theme.SecondaryPink
import com.example.livevoicetranslator_rd.presentation.theme.greenBox
import com.example.livevoicetranslator_rd.presentation.theme.premiumBackgroundColor
import com.example.livevoicetranslator_rd.presentation.theme.primaryPink
import com.example.livevoicetranslator_rd.presentation.theme.purpleBox
import com.example.livevoicetranslator_rd.presentation.theme.secondaryBox
import com.example.livevoicetranslator_rd.presentation.theme.textColour
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import kotlinx.coroutines.launch
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.continue_label
import livevoicetranslatorrd.composeapp.generated.resources.get_started
import livevoicetranslatorrd.composeapp.generated.resources.ic_camera_mini
import livevoicetranslatorrd.composeapp.generated.resources.ic_globe
import livevoicetranslatorrd.composeapp.generated.resources.ic_man
import livevoicetranslatorrd.composeapp.generated.resources.ic_mic
import livevoicetranslatorrd.composeapp.generated.resources.ic_women
import livevoicetranslatorrd.composeapp.generated.resources.on_boarding_description_1
import livevoicetranslatorrd.composeapp.generated.resources.on_boarding_description_2
import livevoicetranslatorrd.composeapp.generated.resources.on_boarding_description_3
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// Data class for nodes
private data class DesignNode(
    val code: String? = null,
    val color: Color,
    val angle: Float,
    val radius: Float,
    val textColor: Color = Color.White,
    val avatar: DrawableResource? = null
)

// Data class for onboarding pages
private data class OnboardingPageData(
    val orbitalConfig: OrbitalConfig,
    val title: AnnotatedString,
    val description: StringResource, // String resource
    val buttonLabel: StringResource, // String resource
    val progressIndex: Int,
    val nextRoute: ScreenRoute?
)

private data class OrbitalConfig(
    val backgroundColor: Color,
    val ringColor: Color,
    val centerColor: Color,
    val centerIcon: DrawableResource,
    val nodes: List<DesignNode>,
    val dots: List<DesignNode>
)

@Composable
fun OnBoardingScreen() {
    val navController = LocalNavController.current
    val pagerState = rememberPagerState(pageCount = { 3 })
    val coroutineScope = rememberCoroutineScope()

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize().background(BackgroundColor)
    ) {
        val isLandscape = maxWidth > maxHeight

        val pages = remember {
            listOf(
                OnboardingPageData(
                    orbitalConfig = OrbitalConfig(
                        backgroundColor = secondaryBox,
                        ringColor = SecondaryColor,
                        centerColor = PrimaryColor,
                        centerIcon = Res.drawable.ic_globe,
                        nodes = listOf(
                            DesignNode("ES", primaryPink, 45f, 112f),
                            DesignNode("GB", premiumBackgroundColor, 225f, 112f, Color.Black)
                        ),
                        dots = listOf(
                            DesignNode("", PrimaryColor, 35f, 75f),
                            DesignNode("", PrimaryColor, 320f, 150f),
                            DesignNode("", PrimaryColor, 200f, 150f)
                        )
                    ),
                    title = buildAnnotatedString {
                        append("Break ")
                        withStyle(SpanStyle(PrimaryColor)) {
                            append("Language")
                        }
                        append(" Barriers\nInstantly")
                    },
                    description = Res.string.on_boarding_description_1,
                    buttonLabel = Res.string.get_started,
                    progressIndex = 0,
                    nextRoute = null // Will be handled in pager
                ), OnboardingPageData(
                    orbitalConfig = OrbitalConfig(
                        backgroundColor = greenBox,
                        ringColor = GreenColor,
                        centerColor = GreenColor,
                        centerIcon = Res.drawable.ic_mic,
                        nodes = listOf(
                            DesignNode(
                                color = primaryPink,
                                angle = 45f,
                                radius = 112f,
                                avatar = Res.drawable.ic_man
                            ), DesignNode(
                                color = primaryPink,
                                angle = 225f,
                                radius = 112f,
                                avatar = Res.drawable.ic_women
                            )
                        ),
                        dots = listOf(
                            DesignNode("", GreenColor, 35f, 75f),
                            DesignNode("", GreenColor, 320f, 150f),
                            DesignNode("", GreenColor, 200f, 150f)
                        )
                    ),
                    title = buildAnnotatedString {
                        append("Speak and ")
                        withStyle(SpanStyle(PrimaryColor)) {
                            append("Translate")
                        }
                        append(" in\nReal Time")
                    },
                    description = Res.string.on_boarding_description_2,
                    buttonLabel = Res.string.continue_label,
                    progressIndex = 1,
                    nextRoute = null
                ), OnboardingPageData(
                    orbitalConfig = OrbitalConfig(
                        backgroundColor = purpleBox,
                        ringColor = SecondaryPink,
                        centerColor = SecondaryPink,
                        centerIcon = Res.drawable.ic_camera_mini,
                        nodes = emptyList(),
                        dots = listOf(
                            DesignNode("", SecondaryPink, 35f, 75f),
                            DesignNode("", SecondaryPink, 320f, 112f),
                            DesignNode("", SecondaryPink, 200f, 112f)
                        )
                    ),
                    title = buildAnnotatedString {
                        withStyle(SpanStyle(PrimaryColor)) {
                            append("Scan")
                        }
                        append(" & Translate\nInstantly")
                    },
                    description = Res.string.on_boarding_description_3,
                    buttonLabel = Res.string.continue_label,
                    progressIndex = 2,
                    nextRoute = ScreenRoute.Main
                )
            )
        }

        if (isLandscape) {
            // Landscape layout
            Row(
                modifier = Modifier.fillMaxSize().padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Orbital system on the left
                HorizontalPager(
                    state = pagerState, modifier = Modifier.weight(1f).aspectRatio(1f)
                ) { pageIndex ->
                    val page = pages[pageIndex]
                    OrbitalSystem(
                        config = page.orbitalConfig, modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(24.dp))

                // Content on the right
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    TitleSection(
                        currentPage = pagerState.currentPage, pages = pages, isLandscape = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PrimaryButton(
                        label = stringResource(pages[pagerState.currentPage].buttonLabel),
                        onClick = {
                            coroutineScope.launch {
                                if (pagerState.currentPage == pages.size - 1) {
                                    navController.navigate(ScreenRoute.Main) {
                                        popUpTo(ScreenRoute.OnBoardingScreen) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    pagerState.animateScrollToPage(
                                        page = pagerState.currentPage + 1, animationSpec = tween(
                                            durationMillis = 300, easing = FastOutSlowInEasing
                                        )
                                    )
                                }
                            }
                        })
                }
            }
        } else {
            // Portrait layout (original)
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                // Horizontal Pager for orbital systems
                HorizontalPager(
                    state = pagerState, modifier = Modifier.fillMaxWidth().weight(1f)
                ) { pageIndex ->
                    val page = pages[pageIndex]
                    OrbitalSystem(
                        config = page.orbitalConfig, modifier = Modifier.fillMaxSize()
                    )
                }

                // Title and description
                TitleSection(
                    currentPage = pagerState.currentPage, pages = pages, isLandscape = false
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Call to action button
                PrimaryButton(
                    label = stringResource(pages[pagerState.currentPage].buttonLabel), onClick = {
                        coroutineScope.launch {
                            if (pagerState.currentPage == pages.size - 1) {
                                // Last page - navigate to main
                                navController.navigate(ScreenRoute.Main) {
                                    popUpTo(ScreenRoute.OnBoardingScreen) {
                                        inclusive = true
                                    }
                                }
                            } else {
                                // Animate to next page
                                pagerState.animateScrollToPage(
                                    page = pagerState.currentPage + 1, animationSpec = tween(
                                        durationMillis = 300, easing = FastOutSlowInEasing
                                    )
                                )
                            }
                        }
                    })

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun OrbitalSystem(
    config: OrbitalConfig, modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "orbital")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f, targetValue = 360f, animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing), repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    Box(
        modifier = modifier.background(config.backgroundColor), contentAlignment = Alignment.Center
    ) {
        // Orbital rings
        OrbitalRings(ringCount = 4, maxRadius = 150.dp, ringColor = config.ringColor)

        // Center globe
        CenterGlobe(color = config.centerColor, icon = config.centerIcon)

        // Orbiting language nodes
        config.nodes.forEach { lang ->
            OrbitingNode(
                language = lang, rotation = rotation
            )
        }

        // Small dots on orbits
        SmallOrbitDots(rotation = rotation, dots = config.dots)
    }
}

@Composable
private fun OrbitalRings(
    ringCount: Int, maxRadius: Dp, ringColor: Color, modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(maxRadius * 2)) {
        val centerX = size.width / 2
        val centerY = size.height / 2

        repeat(ringCount) { index ->
            val radius = (maxRadius.toPx() / ringCount) * (index + 1)
            drawCircle(
                color = ringColor,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = 1f)
            )
        }
    }
}

@Composable
private fun CenterGlobe(
    color: Color, icon: DrawableResource, modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(64.dp).clip(CircleShape).background(
            if (color == PrimaryColor) {
                Brush.radialGradient(
                    colors = listOf(PrimaryColor, SecondaryColor)
                )
            } else {
                Brush.radialGradient(colors = listOf(color, color))
            }
        ), contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon), contentDescription = null, tint = Color.Unspecified
        )
    }
}

@Composable
private fun OrbitingNode(
    language: DesignNode, rotation: Float, modifier: Modifier = Modifier
) {
    val angleRad = (language.angle + rotation).toDouble() * PI / 180.0
    val offsetX = (language.radius * cos(angleRad)).dp
    val offsetY = (language.radius * sin(angleRad)).dp

    Box(
        modifier = modifier.offset(x = offsetX, y = offsetY)
    ) {
        CircleBadge(
            code = language.code,
            color = language.color,
            textColor = language.textColor,
            avatar = language.avatar
        )
    }
}

@Composable
private fun CircleBadge(
    code: String?,
    color: Color,
    textColor: Color = Color.White,
    avatar: DrawableResource? = null,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(48.dp).clip(CircleShape).background(color),
        contentAlignment = Alignment.Center
    ) {
        if (code != null && code.isNotEmpty()) {
            Text(
                text = code, color = textColor, fontSize = 14.sp, fontWeight = FontWeight.Bold
            )
        } else if (avatar != null) {
            Image(
                modifier = Modifier.size(48.dp),
                painter = painterResource(avatar),
                contentDescription = null
            )
        }
    }
}

@Composable
private fun SmallOrbitDots(
    rotation: Float, dots: List<DesignNode>, modifier: Modifier = Modifier
) {
    dots.forEach { dot ->
        val angleRad = (dot.angle + rotation * 0.20).toDouble() * PI / 180.0
        val offsetX = (dot.radius * cos(angleRad)).dp
        val offsetY = (dot.radius * sin(angleRad)).dp

        Box(
            modifier = modifier.offset(x = offsetX, y = offsetY).size(6.dp).clip(CircleShape)
                .background(dot.color)
        )
    }
}

@Composable
private fun TitleSection(
    currentPage: Int,
    pages: List<OnboardingPageData>,
    isLandscape: Boolean = false,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (!isLandscape) {
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Progress indicator
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(bottom = if (isLandscape) 16.dp else 24.dp)
        ) {
            repeat(3) { index ->
                val isSelected = currentPage == index
                Box(
                    modifier = Modifier.width(if (isSelected) 28.dp else 8.dp).height(8.dp)
                        .clip(RoundedCornerShape(8.dp)).background(
                            if (isSelected) PrimaryColor
                            else SecondaryColor.copy(alpha = 0.2f)
                        )
                )
            }
        }

        if (!isLandscape) {
            Spacer(modifier = Modifier.height(32.dp))
        }

        Text(
            text = pages[currentPage].title,
            textAlign = TextAlign.Center,
            color = textColour,
            style = MaterialTheme.typography.displaySmall.copy(
                fontSize = if (isLandscape) 20.sp else 24.sp, fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(if (isLandscape) 12.dp else 16.dp))

        Text(
            text = stringResource(pages[currentPage].description),
            fontSize = if (isLandscape) 13.sp else 15.sp,
            textAlign = TextAlign.Center,
            color = textColour,
            lineHeight = if (isLandscape) 18.sp else 22.sp,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
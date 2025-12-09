package com.example.livevoicetranslator_rd.presentation.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.livevoicetranslator_rd.presentation.theme.PrimaryColor
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_mic
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MicButton(
    color: Color = PrimaryColor,
    onClick: () -> Unit = {},
    onLongClick: () -> Unit = {},
    onLongClickRelease: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(52.dp)
            .shadow(elevation = 8.dp, shape = CircleShape)
            .clip(CircleShape)
            .background(color)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_mic),
            contentDescription = "Voice Input",
            tint = Color(0xFFFFFFFF),
            modifier = Modifier.size(24.dp)
        )
    }
    // Using Box with shadow to create the prominent floating effect
//    Box(
//        modifier = modifier
//            .size(72.dp)
//            .shadow(elevation = 8.dp, shape = CircleShape)
//            .clip(CircleShape)
//            .background(color)
//            .clickable(onClick = onClick)
////            .pointerInput(Unit) {
////                detectTapGestures(
////                    onPress = {
////                        onLongClick()
////                        try {
////                            awaitRelease()
////                        } finally {
////                            onLongClickRelease()
////                        }
////                    }
////                )
////            }
//            .padding(16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Icon(
//            imageVector = Icons.Default.Mic,
//            contentDescription = "Speak",
//            tint = Color.White,
//            modifier = Modifier.size(32.dp)
//        )
//    }
}
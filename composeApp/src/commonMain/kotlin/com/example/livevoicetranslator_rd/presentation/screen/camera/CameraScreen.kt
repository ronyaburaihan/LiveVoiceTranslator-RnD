package com.example.livevoicetranslator_rd.presentation.screen.camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CameraScreen() {
    CameraScreenContent()
}

@Composable
private fun CameraScreenContent(){
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Camera Screen"
        )
    }
}

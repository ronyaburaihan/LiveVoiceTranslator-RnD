package com.example.livevoicetranslator_rd.presentation.screen.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CameraActionBar(
    onSpeakClick: () -> Unit,
    onCopyClick: () -> Unit,
    onSaveClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color.White,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Speak button (prominent)
        Button(
            onClick = onSpeakClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3) // Blue color
            ),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.VolumeUp,
                contentDescription = "Speak",
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Speak",
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        
        // Copy button
        IconButton(
            onClick = onCopyClick,
            modifier = Modifier
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = CircleShape
                )
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ContentCopy,
                contentDescription = "Copy",
                tint = Color.Gray
            )
        }
        
        // Save button
        IconButton(
            onClick = onSaveClick,
            modifier = Modifier
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = CircleShape
                )
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Save,
                contentDescription = "Save",
                tint = Color.Gray
            )
        }
        
        // Share button
        IconButton(
            onClick = onShareClick,
            modifier = Modifier
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = CircleShape
                )
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Share,
                contentDescription = "Share",
                tint = Color.Gray
            )
        }
    }
}

package com.example.livevoicetranslator_rd.core.platform

import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

actual class ImagePicker actual constructor() {
    private var getContent: ActivityResultLauncher<String>? = null
    private var activity: ComponentActivity? = null

    @Composable
    actual fun RegisterPicker(onImagePicked: (ByteArray) -> Unit) {
        val context = LocalContext.current
        activity = context as ComponentActivity

        val launcher = rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                activity?.contentResolver?.openInputStream(it)?.use { stream ->
                    onImagePicked(stream.readBytes())
                }
            }
        }
        getContent = launcher
    }

    actual fun pickImage() {
        checkNotNull(getContent) { "RegisterPicker must be called before pickImage()" }
            .launch("image/*")
    }
}
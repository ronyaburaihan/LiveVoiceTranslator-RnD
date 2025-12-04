package com.example.livevoicetranslator_rd

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.livevoicetranslator_rd.domain.setActivityProvider
import com.example.livevoicetranslator_rd.presentation.app.App
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardProvider
import com.example.livevoicetranslator_rd.presentation.screen.phrases.ClipboardService
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // Initialize clipboard service
        ClipboardProvider.instance = ClipboardService(this)

        loadKoinModules(
            module {
                single<Activity> { this@MainActivity }
            }
        )
        setActivityProvider { this }
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
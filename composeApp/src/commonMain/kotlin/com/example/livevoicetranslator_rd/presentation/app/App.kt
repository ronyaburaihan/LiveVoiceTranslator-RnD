package com.example.livevoicetranslator_rd.presentation.app

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.example.livevoicetranslator_rd.presentation.navigation.AppNavigation
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.screen.camera.result.ResultViewModel
import com.hashtag.generator.ai.post.writer.presentation.theme.TurnerAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(
    resultViewModel: ResultViewModel = koinViewModel()
) {
    TurnerAppTheme {
        val navController = rememberNavController()
        val snackBarHostState = remember { SnackbarHostState() }

        AppNavigation(
            resultViewModel = resultViewModel,
            navController = navController,
            snackBarHostState = snackBarHostState,
            initialRoute = ScreenRoute.OnBoardingScreen,
            appState = AppState.Free
        )
    }
}
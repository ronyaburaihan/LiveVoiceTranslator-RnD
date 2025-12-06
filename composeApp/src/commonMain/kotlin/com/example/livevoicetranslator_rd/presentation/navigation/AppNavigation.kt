package com.example.livevoicetranslator_rd.presentation.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import com.example.livevoicetranslator_rd.presentation.app.AppState
import com.example.livevoicetranslator_rd.presentation.screen.main.MainScreen
import com.example.livevoicetranslator_rd.presentation.screen.offer.OfferScreen
import com.example.livevoicetranslator_rd.presentation.screen.onboard.OnBoardingScreen
import com.example.livevoicetranslator_rd.presentation.screen.phrases.PhraseDetailScreen
import com.example.livevoicetranslator_rd.presentation.screen.phrases.PhrasesScreen
import com.example.livevoicetranslator_rd.presentation.screen.premium.PremiumScreen
import com.example.livevoicetranslator_rd.presentation.screen.referral.ReferralScreen
import com.example.livevoicetranslator_rd.presentation.screen.settings.SettingScreen
import com.example.livevoicetranslator_rd.presentation.util.LocalAppState
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import com.example.livevoicetranslator_rd.presentation.util.LocalSnackBarHostState
import com.example.livevoicetranslator_rd.presentation.util.appNavComposable

@Composable
fun AppNavigation(
    navController: NavHostController,
    snackBarHostState: SnackbarHostState,
    appState: AppState?,
    initialRoute: ScreenRoute,
) {
    Scaffold(
        contentWindowInsets = WindowInsets(0),
        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState,
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .windowInsetsPadding(WindowInsets.ime.add(WindowInsets.navigationBars))
            ) {
                Snackbar(snackbarData = it)
            }
        },
    ) {
        CompositionLocalProvider(
            LocalSnackBarHostState provides snackBarHostState,
            LocalNavController provides navController,
            LocalAppState provides appState
        ) {
            AppNavHost(
                navController = navController,
                initialRoute = initialRoute
            )
        }
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    initialRoute: ScreenRoute
) {
    NavHost(
        navController = navController,
        startDestination = initialRoute,
    ) {
        appNavComposable<ScreenRoute.Main> {
            MainScreen()
        }
        appNavComposable<ScreenRoute.Premium> {
            PremiumScreen()
        }
        appNavComposable<ScreenRoute.Phrases> {
            PhrasesScreen()
        }
        appNavComposable<ScreenRoute.PhraseDetail> { backStackEntry ->
            val categoryTitle = backStackEntry.toRoute<ScreenRoute.PhraseDetail>().categoryTitle
            PhraseDetailScreen(
                categoryTitle = categoryTitle,
            )
        }
        appNavComposable<ScreenRoute.OnBoardingScreen> {
            OnBoardingScreen()
        }
        appNavComposable<ScreenRoute.Referral> {
            ReferralScreen()
        }
        appNavComposable<ScreenRoute.Settings> { backStackEntry ->
            val title = backStackEntry.toRoute<ScreenRoute.Settings>().title
            SettingScreen(
                title = title
            )
        }
        appNavComposable<ScreenRoute.Offer> {
            OfferScreen()
        }

    }
}

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.toRoute
import com.example.livevoicetranslator_rd.presentation.app.AppState
import com.example.livevoicetranslator_rd.presentation.screen.main.MainScreen
import com.example.livevoicetranslator_rd.presentation.screen.onboard.OnBoardingScreenOne
import com.example.livevoicetranslator_rd.presentation.screen.phrases.CategoryData
import com.example.livevoicetranslator_rd.presentation.screen.phrases.PhraseDetailScreen
import com.example.livevoicetranslator_rd.presentation.screen.phrases.PhrasesScreen
import com.example.livevoicetranslator_rd.presentation.screen.premium.PremiumScreen
import com.example.livevoicetranslator_rd.presentation.util.LocalAppState
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import com.example.livevoicetranslator_rd.presentation.util.LocalSnackBarHostState
import com.example.livevoicetranslator_rd.presentation.util.appNavComposable
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_accommodation
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_airport
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_basic
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_emergency
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_favourite
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_others
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_restaurant
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_shopping
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_tourism
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_transport
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_travel
import livevoicetranslatorrd.composeapp.generated.resources.ic_cat_workplace

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
        appNavComposable<ScreenRoute.OnBoardingOne> {
            OnBoardingScreenOne()
        }
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
    }
}

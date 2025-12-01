package com.example.livevoicetranslator_rd.presentation.screen.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.livevoicetranslator_rd.presentation.app.isPremium
import com.example.livevoicetranslator_rd.presentation.component.AppBottomNavigation
import com.example.livevoicetranslator_rd.presentation.component.AppTopBar
import com.example.livevoicetranslator_rd.presentation.component.AppTopBarTitle
import com.example.livevoicetranslator_rd.presentation.component.CustomIconButton
import com.example.livevoicetranslator_rd.presentation.component.PremiumButton
import com.example.livevoicetranslator_rd.presentation.navigation.ScreenRoute
import com.example.livevoicetranslator_rd.presentation.screen.camera.CameraScreen
import com.example.livevoicetranslator_rd.presentation.screen.conversation.ConversationScreen
import com.example.livevoicetranslator_rd.presentation.screen.phrases.PhrasesScreen
import com.example.livevoicetranslator_rd.presentation.screen.translate.TranslateScreen
import com.example.livevoicetranslator_rd.presentation.util.LocalAppState
import com.example.livevoicetranslator_rd.presentation.util.LocalNavController
import com.example.livevoicetranslator_rd.presentation.util.appNavComposable
import com.example.livevoicetranslator_rd.presentation.component.AppDrawer
import kotlinx.coroutines.launch
import livevoicetranslatorrd.composeapp.generated.resources.Res
import livevoicetranslatorrd.composeapp.generated.resources.app_name
import livevoicetranslatorrd.composeapp.generated.resources.ic_hambuger
import livevoicetranslatorrd.composeapp.generated.resources.menu
import livevoicetranslatorrd.composeapp.generated.resources.pro
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainScreen() {
    val navController = LocalNavController.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val onDrawerOpen: () -> Unit = remember {
        { scope.launch { drawerState.open() } }
    }

    val onNavigateBack: () -> Unit = remember {
        { navController.navigateUp() }
    }

    val onDrawerClose: () -> Unit = remember {
        { scope.launch { drawerState.close() } }
    }

    // Handle back press to close drawer if open
    //BackHandler(enabled = drawerState.isOpen, onBack = onDrawerClose)

    ModalNavigationDrawer(
        drawerContent = {
            AppDrawer(
                drawerState,
                scope,
                false
            )
        },
        drawerState = drawerState,
        gesturesEnabled = true
    ) {
        MainScreenContent(
            onNavigationClick = {
                onDrawerOpen()
            },
            onProButtonClick = {
                navController.navigate(ScreenRoute.Premium)
            }
        )
    }
}

@Composable
fun MainScreenContent(
    onNavigationClick: () -> Unit = {},
    onProButtonClick: () -> Unit = {}
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = Modifier.fillMaxSize().systemBarsPadding(),
        topBar = {
            MainTopBar(
                modifier = Modifier,
                onNavigationClick = onNavigationClick,
                onProButtonClick = onProButtonClick
            )
        },
        bottomBar = {
            AppBottomNavigation(
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            NavHost(
                modifier = Modifier.weight(1f),
                navController = navController,
                startDestination = ScreenRoute.Conservation
            ) {
                appNavComposable<ScreenRoute.Conservation> {
                    ConversationScreen()
                }
                appNavComposable<ScreenRoute.Translate> {
                    TranslateScreen()
                }
                appNavComposable<ScreenRoute.Camera> {
                    CameraScreen()
                }
                appNavComposable<ScreenRoute.Phrases> {
                    PhrasesScreen()
                }
            }
        }
    }
}

@Composable
private fun MainTopBar(
    modifier: Modifier,
    title: String = stringResource(Res.string.app_name),
    onNavigationClick: () -> Unit,
    onProButtonClick: () -> Unit
) {
    val appState = LocalAppState.current

    AppTopBar(
        modifier = modifier,
        title = {
            AppTopBarTitle(
                title = title
            )
        },
        navigationIcon = {
            CustomIconButton(
                icon = Res.drawable.ic_hambuger,
                contentDescription = stringResource(Res.string.menu),
                onClick = onNavigationClick
            )
        },
        actions = {
            if (!appState.isPremium) {
                PremiumButton(
                    label = stringResource(Res.string.pro),
                    onClick = onProButtonClick
                )
            }
        },
    )
}
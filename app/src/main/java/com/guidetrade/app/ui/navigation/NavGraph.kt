package com.guidetrade.app.ui.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@Composable
fun GuideTradeNavHost(
    authRepository: com.guidetrade.app.domain.repository.AuthRepository,
    userRepository: com.guidetrade.app.domain.repository.UserRepository,
    watchlistRepository: com.guidetrade.app.domain.repository.WatchlistRepository,
    researchRepository: com.guidetrade.app.domain.repository.ResearchRepository,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = NavRoutes.Auth.route
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute != NavRoutes.Auth.route) {
                CenterAlignedTopAppBar(
                    title = { Text("Guide Trade") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                )
            }
        },
        bottomBar = {
            if (currentRoute != NavRoutes.Auth.route) {
                GuideTradeBottomBar(navController = navController, currentRoute = currentRoute)
            }
        },
        contentWindowInsets = if (currentRoute != NavRoutes.Auth.route) WindowInsets.statusBars else WindowInsets(0)
    ) { innerPadding ->
        val contentModifier = modifier
            .fillMaxSize()
            .padding(innerPadding)

        androidx.navigation.compose.NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = contentModifier,
            route = "main"
        ) {
            com.guidetrade.app.ui.navigation.navGraph(
                navController = navController,
                authRepository = authRepository,
                userRepository = userRepository,
                watchlistRepository = watchlistRepository,
                researchRepository = researchRepository
            )
        }
    }
}

@Composable
fun GuideTradeBottomBar(navController: NavHostController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem("Home", NavRoutes.Home.route),
        BottomNavItem("Research", NavRoutes.Research.route),
        BottomNavItem("Chat", NavRoutes.ChatResults.route),
        BottomNavItem("Watchlist", NavRoutes.Watchlist.route),
        BottomNavItem("Settings", NavRoutes.Settings.route)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { },
                label = { Text(item.label) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.navigationBarItemColors()
            )
        }
    }
}

data class BottomNavItem(val label: String, val route: String)

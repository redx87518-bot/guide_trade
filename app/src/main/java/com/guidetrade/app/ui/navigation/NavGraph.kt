package com.guidetrade.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.ExperimentalMaterial3Api
import com.guidetrade.app.ui.screens.auth.AuthScreen
import com.guidetrade.app.ui.screens.chat.ChatResultsScreen
import com.guidetrade.app.ui.screens.chat.ChatScreen
import com.guidetrade.app.ui.screens.history.HistoryScreen
import com.guidetrade.app.ui.screens.home.HomeScreen
import com.guidetrade.app.ui.screens.reports.ReportsScreen
import com.guidetrade.app.ui.screens.research.ResearchScreen
import com.guidetrade.app.ui.screens.settings.SettingsScreen
import com.guidetrade.app.ui.screens.watchlist.WatchlistScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star

@Composable
fun GuideTradeAppNavHost(
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = setOf(
        NavRoutes.Home.route,
        NavRoutes.Research.route,
        NavRoutes.Watchlist.route,
        NavRoutes.History.route,
        NavRoutes.Settings.route
    )

    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        topBar = {
            if (showBottomBar) {
                CenterAlignedTopAppBar(
                    title = { Text("Guide Trade") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors()
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                GuideTradeBottomBar(
                    navController = navController,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavRoutes.Home.route,
                modifier = Modifier.fillMaxSize()
            ) {
                composable(NavRoutes.Home.route) {
                    HomeScreen(
                        onNavigateToResearch = { navController.navigate(NavRoutes.Research.route) },
                        onNavigateToChat = { navController.navigate(NavRoutes.ChatResults.route) },
                        onNavigateToWatchlist = { navController.navigate(NavRoutes.Watchlist.route) },
                        onNavigateToSettings = { navController.navigate(NavRoutes.Settings.route) }
                    )
                }
                composable(NavRoutes.ChatResults.route) {
                    ChatResultsScreen(
                        onSessionClicked = { sessionId ->
                            navController.navigate(NavRoutes.Chat.createRoute(sessionId))
                        },
                        onNewChatClicked = { }
                    )
                }
                composable(NavRoutes.Chat.route) { backStackEntry ->
                    val sessionId = backStackEntry.arguments?.getString("sessionId") ?: return@composable
                    ChatScreen(
                        sessionId = sessionId,
                        uid = "",
                        onNavigateBack = { navController.popBackStack() }
                    )
                }
                composable(NavRoutes.Research.route) {
                    ResearchScreen()
                }
                composable(NavRoutes.Watchlist.route) {
                    WatchlistScreen(onItemClicked = { _ ->
                        navController.navigate(NavRoutes.Research.route)
                    })
                }
                composable(NavRoutes.History.route) {
                    HistoryScreen(onResearchClicked = { noteId ->
                        navController.navigate(NavRoutes.ResearchResults.createRoute(noteId))
                    })
                }
                composable(NavRoutes.Reports.route) {
                    ReportsScreen(onReportClicked = { noteId ->
                        navController.navigate(NavRoutes.ResearchResults.createRoute(noteId))
                    })
                }
                composable(NavRoutes.Settings.route) {
                    SettingsScreen(
                        onSignOut = onSignOut,
                        onVoiceSettingsClicked = { navController.navigate(NavRoutes.VoiceSettings.route) },
                        onTelegramSettingsClicked = { navController.navigate(NavRoutes.TelegramSettings.route) },
                        onDiscordSettingsClicked = { navController.navigate(NavRoutes.DiscordSettings.route) },
                        onNotificationsSettingsClicked = { navController.navigate(NavRoutes.NotificationsSettings.route) },
                        onHistoryClicked = { navController.navigate(NavRoutes.History.route) },
                        onReportsClicked = { navController.navigate(NavRoutes.Reports.route) }
                    )
                }
                composable(NavRoutes.VoiceSettings.route) {
                    SettingsScreen(onSignOut = onSignOut)
                }
                composable(NavRoutes.TelegramSettings.route) {
                    SettingsScreen(onSignOut = onSignOut)
                }
                composable(NavRoutes.DiscordSettings.route) {
                    SettingsScreen(onSignOut = onSignOut)
                }
                composable(NavRoutes.NotificationsSettings.route) {
                    SettingsScreen(onSignOut = onSignOut)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuideTradeBottomBar(
    navController: NavHostController,
    currentRoute: String?
) {
    val items = listOf(
        BottomNavItem("Home", NavRoutes.Home.route, Icons.Default.Home),
        BottomNavItem("Research", NavRoutes.Research.route, Icons.Default.Search),
        BottomNavItem("Watchlist", NavRoutes.Watchlist.route, Icons.Default.Star),
        BottomNavItem("History", NavRoutes.History.route, Icons.Default.History),
        BottomNavItem("Settings", NavRoutes.Settings.route, Icons.Default.Settings)
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(NavRoutes.Home.route)
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label
                    )
                },
                label = { Text(item.label) },
                colors = NavigationBarItemDefaults.colors()
            )
        }
    }
}

data class BottomNavItem(
    val label: String,
    val route: String,
    val icon: ImageVector
)

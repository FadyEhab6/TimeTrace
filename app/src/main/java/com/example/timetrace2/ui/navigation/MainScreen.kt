package com.example.timetrace2.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.timetrace2.ui.components.BottomNavBar
import com.example.timetrace2.ui.components.NavTab
import com.example.timetrace2.ui.theme.ScreenBackground

/**
 * The "shell" of the app: it owns the bottom bar and the NavHost.
 * Screens never create their own Scaffold/bottom bar - they only draw their content.
 */
@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // Which tab is selected is derived from the current route, so it always stays in sync
    // (including when the user presses the system back button).
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentTab = NavTab.entries.firstOrNull { it.name == backStackEntry?.destination?.route }
        ?: NavTab.Today

    Scaffold(
        containerColor = ScreenBackground,
        bottomBar = {
            BottomNavBar(
                selected = currentTab,
                onSelect = { tab ->
                    navController.navigate(tab.name) {
                        // Don't pile up a new copy of the start screen every time a tab is tapped
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true   // tapping the current tab again does nothing
                        restoreState = true      // returning to a tab restores its scroll position
                    }
                },
            )
        },
    ) { innerPadding ->
        AppNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding),
        )
    }
}

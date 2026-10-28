package com.example.timetrace2.ui.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.timetrace2.ui.components.NavTab
import com.example.timetrace2.ui.components.PlaceholderScreen
import com.example.timetrace2.ui.report.ReportScreen
import com.example.timetrace2.ui.pages.profile.ProfileSettingsScreen

/**
 * Maps each tab to the screen that should be shown for it.
 * Each tab's name ("Today", "Report", ...) is used as its route.
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavTab.Report.name,
        modifier = modifier,
        //no transition animation between switching pages
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(NavTab.Today.name) { PlaceholderScreen("Today") }   // TODO: replace with TodayScreen()
        composable(NavTab.Report.name) { ReportScreen() }
        composable(NavTab.Zones.name) { PlaceholderScreen("Zones") }   // TODO: replace with ZonesScreen()
        composable(NavTab.Profile.name) { ProfileSettingsScreen() } // TODO: replace with ProfileScreen()
    }
}

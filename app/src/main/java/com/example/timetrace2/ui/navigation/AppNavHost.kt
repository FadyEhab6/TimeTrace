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
import com.example.timetrace2.ui.pages.home.HomeScreen
import com.example.timetrace2.ui.pages.profile.ProfileSettingsScreen
import com.example.timetrace2.ui.pages.report.ReportScreen
import com.example.timetrace2.ui.pages.timer.TimerScreen
import com.example.timetrace2.ui.pages.timeline.TimelineScreen

private const val TimerRoute = "timer"
private const val TimelineRoute = "timeline"


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
        startDestination = NavTab.Today.name,
        modifier = modifier,
        //no transition animation between switching pages
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(NavTab.Today.name) {
            HomeScreen(
                onProfileClick = { navController.navigate(NavTab.Profile.name) },
                onActiveTimerClick = { navController.navigate(TimerRoute) },
                onPauseClick = { navController.navigate(TimerRoute) },
                onAddEntryClick = { navController.navigate(TimelineRoute) },
                onStartTrackingClick = { navController.navigate(TimerRoute) },
                onViewAllClick = { navController.navigate(TimelineRoute) },
                onEntryClick = { navController.navigate(TimelineRoute) },
            )
        }
        composable(NavTab.Report.name) { ReportScreen() }
        composable(NavTab.Zones.name) { PlaceholderScreen("Zones") }   // TODO: replace with ZonesScreen()
        composable(NavTab.Profile.name) { ProfileSettingsScreen() } // TODO: replace with ProfileScreen()
        composable(TimerRoute) { TimerScreen() }
        composable(TimelineRoute) { TimelineScreen() }
    }
}

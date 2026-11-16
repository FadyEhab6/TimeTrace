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
import com.example.timetrace2.ui.pages.category.CategoriesScreen
import com.example.timetrace2.ui.report.ReportScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavTab.Categories.name, // Starts directly on Categories!
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(NavTab.Today.name) { PlaceholderScreen("Today") }
        composable(NavTab.Report.name) { ReportScreen() }
        composable(NavTab.Categories.name) { CategoriesScreen() }
        composable(NavTab.Profile.name) { PlaceholderScreen("Profile") }
    }
}
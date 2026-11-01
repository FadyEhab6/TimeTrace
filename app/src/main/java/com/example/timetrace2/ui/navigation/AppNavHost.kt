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
import com.example.timetrace2.ui.pages.timer.TimerScreen

// el navigation — kol tab byftah screen tania
@Composable
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = NavTab.Today.name, // el timer howa el screen el awel
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        // el timer screen — da el screen bta3etna
        composable(NavTab.Today.name) { TimerScreen() }

        // el baqi placeholder lhad ma yetdmg el report branch
        composable(NavTab.Report.name)  { PlaceholderScreen("Report")  }
        composable(NavTab.Zones.name)   { PlaceholderScreen("Zones")   }
        composable(NavTab.Profile.name) { PlaceholderScreen("Profile") }
    }
}

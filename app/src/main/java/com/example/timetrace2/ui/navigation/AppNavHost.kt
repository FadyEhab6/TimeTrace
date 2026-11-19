package com.example.timetrace2.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.timetrace2.ui.components.NavTab
import com.example.timetrace2.ui.pages.category.CategoriesScreen
import com.example.timetrace2.ui.pages.home.HomeScreen
import com.example.timetrace2.ui.pages.profile.ProfileSettingsScreen
import com.example.timetrace2.ui.pages.report.ReportScreen
import com.example.timetrace2.ui.pages.timer.TimerScreen
import com.example.timetrace2.ui.pages.timeline.TimeEntryDialog
import com.example.timetrace2.ui.pages.timeline.TimelineScreen
import com.example.timetrace2.ui.pages.timeline.TimelineViewModel

private const val TimerRoute = "timer"


/**
 * Maps each tab to the screen that should be shown for it.
 * Each tab's name ("Today", "Report", ...) is used as its route.
 */
@Composable
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun AppNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    // 3ashan el home w el timeline yeshterakoo f nafs el data
    val timelineViewModel: TimelineViewModel = viewModel(viewModelStoreOwner = context as ComponentActivity)

    NavHost(
        navController = navController,
        startDestination = NavTab.Home.name,
        modifier = modifier,
        //no transition animation between switching pages
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(NavTab.Home.name) {
            HomeScreen(
                onProfileClick = { navController.navigate(NavTab.Profile.name) },
                onActiveTimerClick = { navController.navigate(TimerRoute) },
                onPauseClick = { navController.navigate(TimerRoute) },
                onAddEntryClick = {
                    // baftah el dialog f nafs el screen من غير ما اروح el timeline
                    var d: android.app.Dialog? = null
                    d = TimeEntryDialog.create(
                        context = context,
                        existing = null,
                        onSave = { cat, title, start, end ->
                            timelineViewModel.saveEntry(cat, title, start, end)
                            d?.dismiss() // ba3d ma save khalas, asker el dialog
                        },
                        onDismiss = {}
                    ).also { it.show() }
                },
                onStartTrackingClick = { navController.navigate(TimerRoute) },
                onViewAllClick = { navController.navigate(NavTab.Timeline.name) },
                onEntryClick = { navController.navigate(NavTab.Timeline.name) },
            )
        }
        // babes el nafs el viewModel 3ashan el entries tetla3 f el timeline ba3den
        composable(NavTab.Timeline.name) { TimelineScreen(viewModel = timelineViewModel) }
        composable(NavTab.Reports.name) { ReportScreen() }
        composable(NavTab.Categories.name) { CategoriesScreen() }
        composable(NavTab.Profile.name) { ProfileSettingsScreen() }
        composable(TimerRoute) { TimerScreen() }
    }
}

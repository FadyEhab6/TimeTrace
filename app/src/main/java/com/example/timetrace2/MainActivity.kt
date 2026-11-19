package com.example.timetrace2

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.timetrace2.ui.navigation.MainScreen
import com.example.timetrace2.ui.pages.onboarding.OnboardingScreen
import com.example.timetrace2.ui.pages.splash.SplashScreen
import com.example.timetrace2.ui.theme.TimeTrace2Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeTrace2Theme(darkTheme = false, dynamicColor = false) {
                val context = LocalContext.current
                val prefs = remember {
                    context.getSharedPreferences("timetrace_profile", Context.MODE_PRIVATE)
                }

                // bashoof lw el user katab esmo abl keda wla la2
                var savedName by remember {
                    mutableStateOf(prefs.getString("name", "") ?: "")
                }
                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(1_500)
                    showSplash = false
                }

                if (showSplash) {
                    SplashScreen()
                } else if (savedName.isBlank()) {
                    // awel mara yedkhol w lesa mafesh esm -> nefta7 el onboarding
                    OnboardingScreen(
                        onNameSaved = { name ->
                            prefs.edit().putString("name", name).apply()
                            savedName = name
                        }
                    )
                } else {
                    MainScreen()
                }
            }
        }
    }
}

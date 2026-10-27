
package com.example.timetrace2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.timetrace2.ui.pages.splash.SplashScreen
import com.example.timetrace2.ui.pages.timer.TimerScreen
import com.example.timetrace2.ui.theme.TimeTrace2Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TimeTrace2Theme {
                var showSplash by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(2000)
                    showSplash = false
                }

                if (showSplash) {
                    SplashScreen()
                } else {
                    TimerScreen()
                }
            }
        }
    }
}

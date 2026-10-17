package com.example.timetrace2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import com.example.timetrace2.ui.pages.splash.SplashScreen
import com.example.timetrace2.ui.pages.timer.TimerScreen
import androidx.activity.enableEdgeToEdge
import com.example.timetrace2.ui.report.ReportScreen
import com.example.timetrace2.ui.theme.TimeTrace2Theme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeTrace2Theme(darkTheme = false, dynamicColor = false) {
                ReportScreen()
            }
        }
    }
}

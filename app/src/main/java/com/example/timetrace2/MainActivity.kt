package com.example.timetrace2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.timetrace2.ui.navigation.MainScreen
import com.example.timetrace2.ui.theme.TimeTrace2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimeTrace2Theme(darkTheme = false, dynamicColor = false) {
                MainScreen()
            }
        }
    }
}

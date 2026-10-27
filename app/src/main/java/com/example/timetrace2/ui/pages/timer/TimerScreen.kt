
package com.example.timetrace2.ui.pages.timer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timetrace2.ui.theme.StudyBlue
import com.example.timetrace2.ui.theme.Typography
import kotlinx.coroutines.delay

@Preview
@Composable
fun TimerScreen() {
    var seconds by remember { mutableIntStateOf(0) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running) {
        while (running) {
            delay(1000)
            seconds++
        }
    }

    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    val time = "%02d:%02d:%02d".format(hours, minutes, secs)

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Study Timer", style = Typography.headlineMedium)

        Spacer(Modifier.height(12.dp))

        Text("First Prototype")

        Spacer(Modifier.height(40.dp))

        Text(time, style = Typography.displayMedium)

        Spacer(Modifier.height(30.dp))

        Button(onClick = { running = !running }, colors = ButtonDefaults.buttonColors(
            containerColor = StudyBlue,
            contentColor = Color.White
        )) {
            Text(if (running) "Pause" else "Start / Resume")
        }

        OutlinedButton(onClick = {
            running = false
            seconds = 0
        }) {
            Text("Stop", color = StudyBlue)
        }
    }
}

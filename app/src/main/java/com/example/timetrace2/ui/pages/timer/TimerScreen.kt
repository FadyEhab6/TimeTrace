package com.example.timetrace2.ui.pages.timer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timetrace2.ui.theme.ScreenBackground
import com.example.timetrace2.ui.theme.StudyBlue
import com.example.timetrace2.ui.theme.TextMuted
import com.example.timetrace2.ui.theme.Typography

// el screen el ra2esiya bta3et el timer
@Composable
fun TimerScreen(vm: TimerViewModel = viewModel()) {

    // gib el values mn el viewmodel 3shan el screen yt update lw etghayaret
    val seconds by vm.seconds.collectAsState()
    val running by vm.running.collectAsState()
    val sessions by vm.sessions.collectAsState()

    // 7awel el seconds l sa3at/da2a2/sanya 3shan net3rd
    val hours   = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs    = seconds % 60
    val timeText = "%02d:%02d:%02d".format(hours, minutes, secs)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(48.dp))

        // 3onwan el screen
        Text("Study Timer", style = Typography.headlineMedium, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(8.dp))

        // status: shaghal / mwa2af / gahz
        Text(
            text = when {
                running    -> "Running…"
                seconds > 0 -> "Paused"
                else        -> "Ready"
            },
            style = Typography.bodyMedium,
            color = TextMuted,
        )

        Spacer(Modifier.height(40.dp))

        // el wa2t el byet3rd el kbeer
        Text(timeText, style = Typography.displayMedium, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(40.dp))

        // zrar el start aw el pause
        Button(
            onClick = { if (running) vm.pause() else vm.start() },
            colors = ButtonDefaults.buttonColors(containerColor = StudyBlue, contentColor = Color.White),
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(if (running) "Pause" else "Start / Resume")
        }

        Spacer(Modifier.height(8.dp))

        // zrar el stop — bytzhr bs lma el timer eshtaghal
        if (seconds > 0 || running) {
            OutlinedButton(
                onClick = { vm.stop() },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text("Stop & Save", color = StudyBlue)
            }
        }

        Spacer(Modifier.height(32.dp))

        // lista bel sessions elly etsa7elet el naharda
        if (sessions.isNotEmpty()) {
            Text(
                text = "Sessions today",
                style = Typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = TextMuted,
                modifier = Modifier.align(Alignment.Start),
            )
            Spacer(Modifier.height(8.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(sessions) { session -> SessionRow(session) }
            }
        }
    }
}

// kol row fel lista — bywri esm el session wel mudda
@Composable
private fun SessionRow(session: TimerSession) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text("Timer Session", style = Typography.bodyMedium)
        Text(session.label, style = Typography.bodyMedium, fontWeight = FontWeight.Medium, color = StudyBlue)
    }
}

package com.example.timetrace2.ui.report

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrace2.data.SampleData
import com.example.timetrace2.ui.components.BottomNavBar
import com.example.timetrace2.ui.components.NavTab
import com.example.timetrace2.ui.theme.ScreenBackground
import com.example.timetrace2.ui.theme.TimeTrace2Theme

@Composable
fun ReportScreen(modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf(NavTab.Report) }

    Scaffold(
        modifier = modifier,
        containerColor = ScreenBackground,
        bottomBar = { BottomNavBar(selected = selectedTab, onSelect = { selectedTab = it }) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "OYT",
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 38.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
            )
            HoursCard(SampleData.weekBar)
            TopZonesCard(SampleData.zones)
            WeeklyRhythmCard(SampleData.week)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 860)
@Composable
private fun ReportScreenPreview() {
    TimeTrace2Theme(darkTheme = false, dynamicColor = false) {
        ReportScreen()
    }
}

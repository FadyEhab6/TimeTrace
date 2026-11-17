package com.example.timetrace2.ui.pages.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrace2.ui.components.SectionCard
import com.example.timetrace2.ui.home.HomeData
import com.example.timetrace2.ui.home.HomeEntry
import com.example.timetrace2.ui.home.SampleHomeData
import com.example.timetrace2.ui.theme.*

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    data: HomeData = SampleHomeData.data,
    onProfileClick: () -> Unit = {},
    onActiveTimerClick: () -> Unit = {},
    onPauseClick: () -> Unit = {},
    onAddEntryClick: () -> Unit = {},
    onStartTrackingClick: () -> Unit = {},
    onViewAllClick: () -> Unit = {},
    onEntryClick: (HomeEntry) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .statusBarsPadding()
            .background(ScreenBackground)
            .padding(horizontal = 14.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        // Greeting + profile
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text("Good morning,", color = TextMuted, fontSize = 14.sp)
                Text(
                    text = data.userName,
                    color = ReportNavy,
                    fontSize = Typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                )
            }
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile",
                tint = ReportBlue,
                modifier = Modifier
                    .size(28.dp)
                    .clickable(onClick = onProfileClick),
            )
        }

        // Today's tracked time + goal
        SectionCard {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Today's tracked time", color = TextMuted, fontSize = 14.sp)
                    Text(
                        text = data.trackedTime,
                        color = ReportNavy,
                        fontSize = Typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text("Goal ${data.goal}", color = ReportBlue, fontSize = 14.sp)
            }
            Spacer(Modifier.height(12.dp))
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFE7ECF2)),
            ) {
                Box(
                    Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(data.progress.coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(50))
                        .background(StudyBlue),
                )
            }
        }

        // Active timer (hidden when nothing is running)
        data.activeTimer?.let { timer ->
            SectionCard(Modifier.clickable(onClick = onActiveTimerClick)) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Column(Modifier.weight(1f)) {
                        Text("Active Timer", color = TextMuted, fontSize = 12.sp)
                        Text("${timer.category} • ${timer.title}", color = ReportNavy, fontSize = 14.sp)
                        Text(
                            text = timer.elapsed,
                            color = ReportNavy,
                            fontSize = Typography.titleMedium.fontSize,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    Box(
                        Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(StudyBlue)
                            .clickable(onClick = onPauseClick),
                        contentAlignment = Alignment.Center,
                    ) {
                       Icon(Icons.Filled.Pause, contentDescription = "Pause", tint = Color.White)
                    }
                }
            }
        }

        // Add Time Entry / Start Tracking buttons
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            ActionCard("Add Time Entry", Icons.Filled.Add, onAddEntryClick, Modifier.weight(1f))
            ActionCard("Start Tracking", Icons.Filled.PlayArrow, onStartTrackingClick, Modifier.weight(1f))
        }

        // Today's entries
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Today's entries",
                color = ReportNavy,
                fontSize = Typography.bodyMedium.fontSize,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "View all",
                color = StudyBlue,
                fontSize = 14.sp,
                modifier = Modifier.clickable(onClick = onViewAllClick),
            )
        }
        SectionCard {
            data.entries.forEachIndexed { index, entry ->
                EntryRow(entry, onClick = { onEntryClick(entry) })
                if (index != data.entries.lastIndex) HorizontalDivider(color = DividerGray)
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun ActionCard(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SectionCard(modifier.clickable(onClick = onClick)) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = StudyBlue,
            modifier = Modifier
                .size(28.dp)
                .align(Alignment.CenterHorizontally),
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            color = ReportNavy,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        )
    }
}

@Composable
private fun EntryRow(entry: HomeEntry, onClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(entry.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center,
        ) {
            Box(Modifier.size(12.dp).clip(CircleShape).background(entry.color))
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(entry.title, color = ReportNavy, fontSize = 15.sp, fontWeight = FontWeight.Medium)
            Text(entry.subtitle, color = TextMuted, fontSize = 12.sp)
        }
        Text(entry.duration, color = ReportNavy, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
        Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
    }
}

@Preview(showBackground = true, widthDp = 400, heightDp = 860)
@Composable
private fun HomeScreenPreview() {
    TimeTrace2Theme(darkTheme = false, dynamicColor = false) {
        HomeScreen()
    }
}

package com.example.timetrace2.ui.pages.timeline

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.timetrace2.ui.theme.*
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.tooling.preview.Preview

private val rangeFormat = DateTimeFormatter.ofPattern("h:mm")

@Composable
fun TimelineScreen(viewModel: TimelineViewModel = viewModel()) {
    val entries by viewModel.entries.collectAsStateWithLifecycle()
    val showDialog by viewModel.showAddDialog.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(horizontal = 20.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Timeline", fontSize = 26.sp, fontWeight = FontWeight.Bold)
            IconButton(onClick = viewModel::openAddDialog) {
                Icon(Icons.Default.Add, contentDescription = "Add time entry", tint = HomeBlue)
            }
        }

        // Date selector (static "Today" for now)
        Row(
            modifier = Modifier.padding(vertical = 8.dp).clickable { /* TODO date picker */ },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Today", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Icon(Icons.Default.ExpandMore, contentDescription = null)
        }

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(entries, key = { it.id }) { entry ->
                EntryRow(entry)
                HorizontalDivider(color = DividerGray)
            }
        }
    }

    if (showDialog) {
        AddTimeEntryDialog(
            onDismiss = viewModel::closeAddDialog,
            onSave = viewModel::addEntry
        )
    }
}

@Composable
private fun EntryRow(entry: TimeEntry) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* TODO: open entry details */ }
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(entry.category.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(entry.category.icon, contentDescription = null, tint = entry.category.color)
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(entry.title, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Text(
                "${entry.category.label}  •  ${entry.start.format(rangeFormat)} - ${entry.end.format(rangeFormat)}",
                color = TextMuted, fontSize = 13.sp
            )
        }
        Text(entry.durationLabel(), fontWeight = FontWeight.SemiBold)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun TimelineScreenPreview() {
    TimelineScreen()
}

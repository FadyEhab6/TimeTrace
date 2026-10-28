package com.example.timetrace2.ui.pages.timeline

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.timetrace2.ui.theme.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter

private val timeFormat = DateTimeFormatter.ofPattern("hh:mm a")

@Composable
fun AddTimeEntryDialog(
    onDismiss: () -> Unit,
    onSave: (EntryCategory, String, LocalTime, LocalTime) -> Unit
) {
    var category by remember { mutableStateOf(EntryCategory.STUDY) }
    var title by remember { mutableStateOf("") }
    var start by remember { mutableStateOf(LocalTime.of(9, 0)) }
    var end by remember { mutableStateOf(LocalTime.of(11, 0)) }

    val titleError = title.isBlank()
    val timeError = !end.isAfter(start)

    Dialog(onDismissRequest = onDismiss) {
        Surface(shape = RoundedCornerShape(20.dp), color = MaterialTheme.colorScheme.surface) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Add Time Entry", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge)
                    IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, "Close") }
                }

                Text("Category", fontWeight = FontWeight.SemiBold)
                CategoryDropdown(category) { category = it }

                Text("Title *", fontWeight = FontWeight.SemiBold)
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = { Text("e.g. Graduation Project") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Start Time", fontWeight = FontWeight.SemiBold)
                        TimeField(start) { start = it }
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("End Time", fontWeight = FontWeight.SemiBold)
                        TimeField(end) { end = it }
                    }
                }
                if (timeError) {
                    Text("End time must be after start time", color = MaterialTheme.colorScheme.error)
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Cancel") }
                    Button(
                        onClick = { onSave(category, title, start, end) },
                        enabled = !titleError && !timeError,
                        modifier = Modifier.weight(1f).height(48.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) { Text("Save Entry") }
                }
            }
        }
    }
}

@Composable
private fun CategoryDropdown(selected: EntryCategory, onSelect: (EntryCategory) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedCard(
            onClick = { expanded = true },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, DividerGray),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(selected.icon, null, tint = selected.color)
                Spacer(Modifier.width(10.dp))
                Text(selected.label, modifier = Modifier.weight(1f))
                Icon(Icons.Default.ExpandMore, null)
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            EntryCategory.values().forEach { c ->
                DropdownMenuItem(
                    text = { Text(c.label) },
                    leadingIcon = { Icon(c.icon, null, tint = c.color) },
                    onClick = { onSelect(c); expanded = false }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeField(time: LocalTime, onChange: (LocalTime) -> Unit) {
    var showPicker by remember { mutableStateOf(false) }

    OutlinedCard(
        onClick = { showPicker = true },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, DividerGray),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(time.format(timeFormat), modifier = Modifier.weight(1f))
            Icon(Icons.Outlined.Schedule, null, tint = TextMuted)
        }
    }

    if (showPicker) {
        val state = rememberTimePickerState(time.hour, time.minute, is24Hour = false)
        AlertDialog(
            onDismissRequest = { showPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    onChange(LocalTime.of(state.hour, state.minute))
                    showPicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showPicker = false }) { Text("Cancel") } },
            text = { TimePicker(state = state) }
        )
    }
}
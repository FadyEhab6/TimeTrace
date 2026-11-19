package com.example.timetrace2.ui.pages.category

import androidx.compose.ui.graphics.Color

enum class CategoryPeriod(val label: String) {
    Today("Today"),
    ThisWeek("This week"),
    ThisMonth("This month"),
    Custom("Custom date"),
}

data class CategoryItemUi(
    val id: Int,
    val name: String,
    val description: String, // raw value, used to prefill the edit dialog
    val subtitle: String,    // display value with "No tasks yet" fallback
    val duration: String,
    val colorHex: String,
    val color: Color,
)

val PresetCategoryColors = listOf(
    "#4F86F7" to Color(0xFF4F86F7), // Study blue
    "#FFB66D" to Color(0xFFFFB66D), // Work orange
    "#78D0B0" to Color(0xFF78D0B0), // Health green
    "#AD8AE4" to Color(0xFFAD8AE4), // Leisure purple
    "#EB5757" to Color(0xFFEB5757), // Red
    "#F0B84A" to Color(0xFFF0B84A), // Yellow
)
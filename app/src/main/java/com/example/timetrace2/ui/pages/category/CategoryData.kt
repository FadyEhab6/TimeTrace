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
    val subtitle: String,
    val duration: String,
    val colorHex: String,
    val color: Color,
)

val PresetCategoryColors = listOf(
    "#4A80F0" to Color(0xFF4A80F0), // Blue
    "#A174ED" to Color(0xFFA174ED), // Purple
    "#5ABF88" to Color(0xFF5ABF88), // Green
    "#F0B84A" to Color(0xFFF0B84A), // Yellow/Orange
    "#E59C52" to Color(0xFFE59C52), // Orange
    "#EB5757" to Color(0xFFEB5757), // Red/Pink
)
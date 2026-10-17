package com.example.timetrace2.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.ShoppingCart
import com.example.timetrace2.ui.theme.DiningBrown
import com.example.timetrace2.ui.theme.HomeBlue
import com.example.timetrace2.ui.theme.ShoppingAmber
import com.example.timetrace2.ui.theme.SleepPurple
import com.example.timetrace2.ui.theme.WorkRed

//Hard coded data for preview
object SampleData {

    val zones = listOf(
        Zone("Sand Hill", "64.3h", HomeBlue, Icons.Filled.Home),
        Zone("Sand Hill Road", "38.4h", WorkRed, Icons.Filled.Place),
        Zone("Nobu Malibu", "4.2h", DiningBrown, Icons.Filled.Favorite),
        Zone("Rodeo Drive", "2.9h", ShoppingAmber, Icons.Filled.ShoppingCart),
    )

    val weekBar = listOf(
        BarPart(SleepPurple, 164f),
        BarPart(HomeBlue, 213f),
        BarPart(WorkRed, 127f),
        BarPart(DiningBrown, 33f),
        BarPart(ShoppingAmber, 14f),
    )

    val week = listOf(
        DaySummary("Sun", listOf(Segment(HomeBlue, 16.8f))),
        DaySummary("Mon", listOf(Segment(DiningBrown, 1.5f), Segment(HomeBlue, 7.3f), Segment(WorkRed, 8.0f))),
        DaySummary("Tue", listOf(Segment(DiningBrown, 1.4f), Segment(HomeBlue, 7.0f), Segment(WorkRed, 8.3f))),
        DaySummary("Wed", listOf(Segment(DiningBrown, 2.0f), Segment(HomeBlue, 7.2f), Segment(WorkRed, 7.5f))),
        DaySummary("Thu", listOf(Segment(DiningBrown, 1.7f), Segment(WorkRed, 6.4f), Segment(HomeBlue, 8.7f))),
        DaySummary(
            "Fri",
            listOf(Segment(ShoppingAmber, 1.2f), Segment(DiningBrown, 1.5f), Segment(HomeBlue, 4.2f), Segment(WorkRed, 9.9f))
        ),
        DaySummary("Sat", listOf(Segment(DiningBrown, 2.3f), Segment(ShoppingAmber, 2.8f), Segment(HomeBlue, 11.6f))),
    )
}

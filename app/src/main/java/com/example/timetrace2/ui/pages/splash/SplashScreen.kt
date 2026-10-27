
package com.example.timetrace2.ui.pages.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.timetrace2.ui.theme.Typography

@Preview
@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("◷", style = Typography.displayLarge)

        Spacer(Modifier.height(16.dp))

        Text("TimeTrace", style = Typography.headlineLarge)

        Spacer(Modifier.height(8.dp))

        Text("Track your time,")
        Text("shape your day")

        Spacer(Modifier.height(60.dp))

        LinearProgressIndicator(  //loading under the name
            modifier = Modifier.width(100.dp)
        )
    }
}

package com.example.timetrace2.ui.pages.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.timetrace2.ui.theme.ReportNavy
import com.example.timetrace2.ui.theme.ScreenBackground

@Composable
fun OnboardingScreen(onNameSaved: (String) -> Unit) {
    // state ll esm elly el user haydakhlo
    var input by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ScreenBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to TimeTrace",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = ReportNavy
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Please enter your name to get started",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Your name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                // btaaked eno msh fady w ba3den a-save
                if (input.isNotBlank()) {
                    onNameSaved(input.trim())
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Get Started")
        }
    }
}

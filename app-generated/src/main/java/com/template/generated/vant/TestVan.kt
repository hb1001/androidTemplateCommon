package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight


@Composable
fun DemoSection(title: String, padding: Boolean = false, content: @Composable () -> Unit) {
    Column {
        Text(
            title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .background(Color.White)
                .padding(12.dp)
        ) {
            Column {
                content()
            }
        }
    }
}
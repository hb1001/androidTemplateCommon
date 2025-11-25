package com.template.feature.setting.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.template.core.ui.components.CommonTitleBar


@Composable
fun SettingSingleItemPage(
    title: String,
    initialValue: String,
    hint: String = "",
    onConfirm: (String) -> Unit,
) {
    var text by remember { mutableStateOf(initialValue) }

    Scaffold(
        topBar = {
            CommonTitleBar(
                title = title,
                showBack = true,
                showConfirm = true,
                onConfirmClick = { onConfirm(text) }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                label = { Text(title) },
                placeholder = { Text(hint) }
            )

            if (hint.isNotEmpty()) {
                Text(
                    text = hint,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
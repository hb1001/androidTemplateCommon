package com.template.feature.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.core.model.Post
import com.template.core.model.Reactions
import com.template.core.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToPagingAndRefresh: () -> Unit,
    onNavigateToPagingOnly: () -> Unit,
    onNavigateToPullRefreshOnly: () -> Unit,
    onNavigateToHomeList: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Button(onClick = onNavigateToPagingAndRefresh) {
                    Text(text = "分页刷新")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onNavigateToPagingOnly) {
                    Text(text = "分页")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onNavigateToPullRefreshOnly) {
                    Text(text = "刷新")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onNavigateToHomeList) {
                    Text(text = "list")
                }
            }
        }
    }
}
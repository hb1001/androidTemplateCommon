package com.template.feature.ai

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.template.core.ui.vant.VanTabbar
import com.template.core.ui.vant.VanTabbarItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiScreen() {
    var activeTab by remember { mutableStateOf("home") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AiScreen") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF04142)
                )
            )
        },
        bottomBar = {
            VanTabbar(
                value = activeTab,
                onChange = { activeTab = it as String },
                activeColor = Color(0xFFF04142),
                inactiveColor = Color.Black
            ) {
                VanTabbarItem(name = "home", icon = {
                    Icon(Icons.Default.Home, contentDescription = "头条")
                }) {
                    Text("头条")
                }
                VanTabbarItem(name = "video", icon = {
                    Icon(Icons.Default.PlayArrow, contentDescription = "视频")
                }) {
                    Text("视频")
                }
                VanTabbarItem(name = "settings", icon = {
                    Icon(Icons.Default.Settings, contentDescription = "放映厅")
                }) {
                    Text("放映厅")
                }
                VanTabbarItem(name = "profile", icon = {
                    Icon(Icons.Default.ThumbUp, contentDescription = "我的")
                }) {
                    Text("我的")
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Text("内容区域")
        }
    }
}

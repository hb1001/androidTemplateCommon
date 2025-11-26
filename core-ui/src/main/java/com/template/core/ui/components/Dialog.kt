package com.template.core.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LogoutDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss, // 点击遮罩或返回键触发
        title = { Text(text = "提示") },
        text = { Text(text = "确定要退出登录吗？") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("退出", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("取消")
            }
        }
    )
}

@Composable
fun CustomLoadingDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        // DialogProperties 很重要，可以配置是否允许点击外部关闭、是否全屏宽度等
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false // 设为 false 可以让弹框宽度撑满屏幕
        )
    ) {
        // 这里必须自己写背景容器，否则是透明的
        Card(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(16.dp))
                Text("正在加载数据...")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentBottomSheet(
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        // 底部弹窗的内容
        Column(modifier = Modifier.padding(bottom = 30.dp)) {
            Text("选择支付方式", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
            ListItem(headlineContent = { Text("微信支付") })
            ListItem(headlineContent = { Text("支付宝") })
        }
    }
}

@Composable
fun DropdownExample() {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.MoreVert, contentDescription = "More")
        }

        // DropdownMenu 内部其实就是基于 Popup 实现的
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("设置") },
                onClick = { /* ... */ }
            )
            DropdownMenuItem(
                text = { Text("关于") },
                onClick = { /* ... */ }
            )
        }
    }
}


// n选1
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetSelector(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        // 底部弹窗的内容
        Column(modifier = Modifier.padding(bottom = 30.dp)) {
            Text(title, style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
            for (option in options){
                ListItem(
                    headlineContent = { Text(option) },
                    modifier = Modifier.clickable {
                        onOptionSelected(option)
                        onDismiss()
                    }
                )
            }
        }
    }
}
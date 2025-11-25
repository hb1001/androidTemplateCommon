package com.template.feature.setting.components
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

// 1. 定义菜单项数据模型
data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)


// --- 组件封装：设置组 ---
@Composable
fun SettingsGroup(
    title: String,
    items: List<ProfileMenuItem>
) {
    Column {
        // 组标题
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )

        // 渲染该组下的所有 Item
        items.forEach { item ->
            SettingsItemRow(item)
        }
    }
}

// --- 组件封装：单行设置 ---
@Composable
fun SettingsItemRow(item: ProfileMenuItem) {
    ListItem(
        headlineContent = { Text(item.title) },
        leadingContent = {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title
            )
        },
        trailingContent = {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        modifier = Modifier.clickable { item.onClick() },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent // 保持透明，使用父容器背景
        )
    )
}
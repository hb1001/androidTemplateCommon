package com.template.generated.component
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// 1. 定义菜单项数据模型
data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val onClick: () -> Unit
)

// --- 组件封装：头部 ---
@Composable
fun ProfileHeader(
    name: String,
    email: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 头像 (如果没有网络图片，用图标代替)
        Surface(
            modifier = Modifier.size(72.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            // 实际项目中这里通常用 Coil 的 AsyncImage
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Avatar",
                modifier = Modifier.padding(12.dp).fillMaxSize(),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // 右侧箭头
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "View Profile",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

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
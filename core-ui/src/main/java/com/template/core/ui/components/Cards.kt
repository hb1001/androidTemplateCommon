package com.template.core.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.template.core.ui.uimodel.CardItem

@Composable
fun SimpleCard(item: CardItem, onClick: () -> Unit = {}) {
    Card(
        // 【关键点1】直接使用 Card 自带的 onClick 参数
        // 这样整个卡片（包括背景色区域）都会响应点击，并且有水波纹效果
        onClick = onClick,

        modifier = Modifier
            .fillMaxWidth() // 列表项通常用 fillMaxWidth 而不是 fillMaxSize
            // 【关键点2】这里的 padding 是“外边距”(Margin)
            // 它负责把卡片和屏幕边缘隔开，这部分是白色的(透明的)，不可点击
            .padding(horizontal = 16.dp, vertical = 8.dp),

        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        // 确保使用我们在 Theme 里定义的灰色背景 (surfaceVariant)
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        // 【关键点3】真正的“内边距”写在这里！
        // 因为 Column 在 Card 内部，所以这 16.dp 的区域会有 Card 的灰色背景
        // 并且因为它们属于 Card 的内容，所以点击时也会响应
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp) // 这里的 padding 会撑大卡片，且拥有背景色
        ) {
            Text(
                text = item.title ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            // 增加一点间距
            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.description ?: "",
                maxLines = 3,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = item.value ?: "",
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
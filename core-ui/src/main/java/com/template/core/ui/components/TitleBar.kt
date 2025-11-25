package com.template.core.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

/**
 * 通用标题栏
 *
 * @param title 标题文字 (居左显示)
 * @param showBack 是否显示返回图标 (默认显示)
 * @param onBackClick 返回点击事件
 * @param showSearch 是否显示搜索图标 (默认不显示)
 * @param onSearchClick 搜索点击事件
 * @param showMore 是否显示更多(三点)图标 (默认显示)
 * @param onMoreClick 更多点击事件
 * @param backgroundColor 背景颜色 (默认白色)
 * @param contentColor 图标和文字颜色 (默认深灰色)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommonTitleBar(
    modifier: Modifier = Modifier,
    title: String = "",
    // 1. 返回控制
    showBack: Boolean = true,
    onBackClick: () -> Unit = {},
    // 3. 搜索控制
    showSearch: Boolean = false,
    onSearchClick: () -> Unit = {},
    // 4. 更多控制
    showDropDown: Boolean = false,
    dropdownMenuComponent: @Composable (close:()->Unit) -> Unit = {},

    showConfirm: Boolean = false,
    onConfirmClick: () -> Unit = {},
    // 样式配置
    contentColor: Color = TopAppBarDefaults.topAppBarColors().titleContentColor
) {

    var expanded by remember { mutableStateOf(false) }
    TopAppBar(
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent, // surface
            titleContentColor = contentColor,
            navigationIconContentColor = contentColor, // onSurface
            actionIconContentColor = contentColor
        ),
        // 2. 标题文字
        title = {
            if (title.isNotEmpty()) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                )
            }
        },
        // 左侧图标区 (返回)
        navigationIcon = {
            if (showBack) {
                IconButton(onClick = onBackClick) {
                    // 截图中的图标是一个"<"形状，KeyboardArrowLeft最像
                    // 如果需要带尾巴的箭头，可以使用 Icons.AutoMirrored.Filled.ArrowBack
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        // 可以适当调整图标大小以完全复刻截图
                        // modifier = Modifier.size(32.dp)
                    )
                }
            }
        },
        // 右侧图标区 (搜索 + 更多)
        actions = {
            // 搜索图标
            if (showSearch) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                }
            }
            // 更多图标
            if (showDropDown) {
                Box() {
                    IconButton(onClick = {expanded = true}) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        dropdownMenuComponent{expanded = false}
                    }
                }
            }
            // 确认图标
            if (showConfirm) {
                // 文字按钮
                TextButton(onClick = onConfirmClick) {
                    Text(text = "确认")
                }
            }
        }
    )
}
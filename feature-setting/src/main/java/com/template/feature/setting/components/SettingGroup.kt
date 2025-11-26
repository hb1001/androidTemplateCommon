package com.template.feature.setting.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.template.core.navigation.listenResult
import com.template.core.ui.components.BottomSheetSelector
import com.template.feature.setting.navigation.navigateToSettingSingle


sealed class ProfileMenuItem(
    val title: String,
    val desc: String? = null,
    val icon: ImageVector? = null
) {
    class SwitchItem(
        title: String,
        desc: String? = null,
        icon: ImageVector? = null,
        val checked: Boolean,
        val onChecked: (Boolean) -> Unit
    ) : ProfileMenuItem(title, desc, icon)

    class TextItem(
        title: String,
        desc: String? = null,
        icon: ImageVector? = null,
        val value: String,
        val onSetValue: (String) -> Unit
    ) : ProfileMenuItem(title, desc, icon)


    class IntItem(
        title: String,
        desc: String? = null,
        icon: ImageVector? = null,
        val value: Int,
        val onSetValue: (Int) -> Unit
    ) : ProfileMenuItem(title, desc, icon)

    class ItemPicker(
        title: String,
        desc: String? = null,
        icon: ImageVector? = null,
        val options: List<String>,
        val value: String,
        val onSetValue: (String) -> Unit
    ) : ProfileMenuItem(title, desc, icon)

    class NormalItem(
        title: String,
        desc: String? = null,
        icon: ImageVector? = null,
        val onClick: () -> Unit
    ) : ProfileMenuItem(title, desc, icon)

    fun showArrow() = when(this){
        is SwitchItem -> false
        is TextItem -> true
        is IntItem -> true
        is ItemPicker -> true
        is NormalItem -> true
    }
    fun itemRightValue() = when(this){
        is TextItem -> this.value
        is IntItem -> this.value.toString()
        is ItemPicker -> this.value
        else -> null
    }
}




// 1. 定义菜单项数据模型
//data class ProfileMenuItem(
//    val title: String,
//    val icon: ImageVector? = null, // 有就显示
//    val description: String? = null,// 有就显示
//    val onSetValue: (String) -> Unit = {},
//    val onClick: (() -> Unit)? = null,
//    val valueType: ValueType = ValueType.None, // None->onCLick, 其他->value
//    val value: String? = null
//)

enum class ValueType {
    None,
    Text,
    Switch,
    ItemPicker,
//    Slider,
//    Dropdown,
//    ColorPicker,
//    DatePicker,
//    TimePicker,
//    DateTimePicker,
//    FilePicker,
//    ImagePicker,
//    CameraPicker,
//    VideoPicker,
//    AudioPicker,
//    ContactPicker,
//    LocationPicker,
//    MapPicker,
//    PhonePicker,
//    EmailPicker,
//    UrlPicker,
//    PasswordPicker,
//    NumberPicker,
}


// --- 组件封装：设置组 ---
@Composable
fun SettingsGroup(
    title: String?,
    items: List<ProfileMenuItem>,
    navController: NavController
) {
    Column {
        // 组标题
        if(title!=null){
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
            )
        }

        // 渲染该组下的所有 Item
        items.forEach { item ->
            SettingsItemRow(item, navController)
        }
    }
}

// --- 组件封装：单行设置 ---
@Composable
fun SettingsItemRow(item: ProfileMenuItem, navController: NavController) {
    var showDialog by remember { mutableStateOf(false) }
    navController.listenResult<String>(item.title) {
        if(item is ProfileMenuItem.TextItem){
            item.onSetValue(it)
        }
        if(item is ProfileMenuItem.IntItem){
            it.toIntOrNull()?.let {value->
                item.onSetValue(value)
            }
        }
    }
    if(item is ProfileMenuItem.ItemPicker){
        if(showDialog){
            BottomSheetSelector(
                title = item.title,
                options = item.options,
                selectedOption = item.value,
                onOptionSelected = {
                    item.onSetValue(it)
                },
                onDismiss = {
                    showDialog = false
                }
            )
        }
    }
    ListItem(
        headlineContent = { Text(item.title) },

        supportingContent = {
            if (item.desc != null) {
                Text(item.desc)
            }
        }, // 可选：如果你想要描述
        leadingContent = {
            if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title
                )
            }
        },
        trailingContent = {
            Row {
                if(item.itemRightValue()!= null){
                    Text(item.itemRightValue()!!)
                }
                if(item is ProfileMenuItem.SwitchItem){
                    Switch(
                        checked = item.checked,
                        onCheckedChange = item.onChecked
                    )
                }
                // todo 其他
                if(item.showArrow()){
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }

            }
        },
        modifier = Modifier.clickable {
            if(item is ProfileMenuItem.TextItem){
                navController.navigateToSettingSingle(item.title, item.value)
            }
            if(item is ProfileMenuItem.SwitchItem){
                item.onChecked(!item.checked)
            }
            if(item is ProfileMenuItem.ItemPicker){
                showDialog = true
            }
            if(item is ProfileMenuItem.IntItem){
                navController.navigateToSettingSingle(item.title, item.value.toString())
            }
            if(item is ProfileMenuItem.NormalItem){
                item.onClick()
            }

        },
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent // 保持透明，使用父容器背景
        )
    )
}

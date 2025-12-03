package com.template.generated.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.template.core.navigation.AppRoutes
import com.template.core.navigation.LocalNavController
import com.template.core.ui.components.CommonTitleBar
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.feature.atrust.navigation.navigateToLoginWithVpn
import com.template.generated.navigateToTestDetail

// 定义菜单数据
data class CellItem(val title: String, val path: String)

val demoCells = listOf(
    CellItem("标签栏 tabbar", "tabbar"),
    CellItem("标签页 tabs", "tabs"),
    CellItem("空状态 empty", "empty"), // 有问题，
    CellItem("步进器 stepper", "stepper"), // 有问题，
    CellItem("Button 按钮", "button"),
    CellItem("Cell 单元格", "cell"),
    CellItem("Typography 文本", "typography"),
    CellItem("Checkbox 复选框", "checkbox"),
    CellItem("Radio 单选框", "radio"),
    CellItem("Input 输入框", "input"), // 有问题
    CellItem("Search 搜索", "search"),
    CellItem("Slider 滑块", "slider"),
    CellItem("Switch 开关", "switch"),
    CellItem("SwipeCell 滑动单元格", "swipeCell"),
    CellItem("Tag 标签", "tag"),
    CellItem("Badge 徽标", "badge"),
    CellItem("Collapse 折叠面板", "collapse"),
    CellItem("Swipe 轮播", "swipe"),
    CellItem("image 图片", "image"),  // 有问题，
    CellItem("icon 图标", "icon"),
    CellItem("Dialog 弹出框", "dialog"),
    CellItem("popup 弹出", "popup"), // 可能只需要底部的
    CellItem("ActionSheet 动作面板", "actionSheet"), //稍微丑，可以参考闹钟的设计
    CellItem("share 分享", "share"), // 自己写
    CellItem("Picker 选择器", "timePicker"), // 时间日期选择器；地区选择；省市区选择
    CellItem("Loading 加载", "loading"), // 弹框还不够
    CellItem("toast 通知提示", "toast"),// 自己写? 有三种：纯文字toast、带图标的成功失败的toast
    /**
     * ~grid\popover\ imagepreview\ \\divider 、 DropdownMenu
     * sidebar\indexbar\ progress\steps
     */
)
// 调整依赖gradle文件

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestListScreen() {
    val navController = LocalNavController.current

    Scaffold(
        topBar = {
            CommonTitleBar(title = "Vant组件列表", showBack = true)
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF7F8FA))
                .verticalScroll(rememberScrollState())
                .padding(bottom = 40.dp)
        ) {
            VanCell(
                title = "登录",
                isLink = true,
                onClick = {
//                    navController.navigateToLoginWithVpn()
                    navController.navigate(AppRoutes.LOGIN_WITH_VPN_ROUTE)
                }
            )
            VanCellGroup(title = "基础组件") {
                demoCells.forEach { item ->
                    VanCell(
                        title = item.title,
                        isLink = true,
                        onClick = {
                            navController.navigateToTestDetail(item.path)
                        }
                    )
                }
            }
        }
    }
}
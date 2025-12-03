# 项目结构说明（优化后的正式版）

本项目采用 **多模块 + Compose + Clean 架构** 的形式，目标是打造一个可学习、可扩展、可自动化出码的 Android 模板工程。

该模板包含高内聚、低耦合的模块划分，通过合理的边界设计，让开发者可以在不同使用场景下灵活组合、快速构建 App。

---

## 一、使用方式

本项目主要有两种使用方式：

### **1. 学习与开发**

作为示例工程（Demo）使用，可：

* 按需修改已有模块
* 自行扩展功能
* 运行并观察最佳实践写法
* 作为企业级项目模板快速启动 App 开发

项目结构清晰、代码质量高，非常适合作为团队统一开发规范的参考。

---

### **2. 出码（Code Generation）**

本项目也作为出码平台的基础模板：

通过运行 `codegen` 模块中的脚本，可以自动生成业务代码，例如：

* 页面框架
* 路由入口
* 网络模型
* 列表页面
* 设置页面结构
  ……等

适用于低代码 / 配置化开发场景。

---

## 二、模块分类

本项目的模块体系主要分为以下几大类：

---

### **1. app 模块（运行模块）**

每一个 app 模块表示一个独立可运行的应用。

可选择是否使用：

* feature 模块提供的业务功能
* core-ui 中提供的通用组件
  也可从零开始自行搭建页面。

---

### **2. core 模块（框架能力）**

提供基础框架能力，包括：

* 通用工具方法
* 通用模型类
* 导航（Navigation）
* 基础架构封装
* 通用组件（非视觉类）
* 公共常量、扩展函数

属于平台核心模块。

---

### **3. data 模块（数据能力）**

提供数据层能力，包括：

* 网络请求封装（Retrofit/Ktor）
* 数据库访问（Room/DataStore）
* 文件存储
* 缓存
* 数据模型、Repository
* 组织架构、人员数据、消息数据等（后续会加入）

data 模块通常为 feature 模块和 core-ui 的数据来源。

---

### **4. feature 模块（功能模块）**

用于封装独立业务功能，app 可直接使用。

当前 feature 模块包括：

1. **login-usm**：USM 登录模块
2. **login-atrust**：含 VPN 的 USM 登录
3. **map**：Mapbox 地图模块
4. **webview**：统一 WebView 模块

每个 feature 均可单独维护、独立测试、可直接组合到不同应用中。

---

### **5. codegen 模块（出码模块）**

内含脚本，用于根据配置自动生成：

* 页面模板
* 数据模型
* UI 构建代码
* 路由注册
* 列表/表单页面

用于提高开发效率、减少样板代码。

---

## 三、模块能力总览

在搭建 App 时，开发者可选择：

✔ 直接使用 feature 模块
✔ 使用 core-ui 的通用组件
✔ 也可完全自定义开发

### **feature 模块提供的能力：**

1. **login-usm**：USM 登录
2. **login-atrust**：VPN + USM 登录
3. **map**：Mapbox 地图
4. **webview**：统一 Web 容器

---

### **core-ui 模块提供的能力：**

1. **表单引擎**
   通过 JSON 或结构化描述自动生成表单页面。

2. **弹框能力**
   快速构建提示框、确认框、选择框等。

3. **标题栏组件**
   统一样式的 AppBar/TopBar。

4. **列表组件**
   快速构造带下拉刷新、分页加载的列表。

5. **设置页面生成器**
   通过配置构建设置页面，多用于系统设置、账号设置等。

6. **通用组件（TODO）**
   如：通用按钮、状态页面、卡片组件等。

---

## 四、后续规划中的能力

未来将补齐以下模块：

---

### ✔ **data-network 方向（数据 & 网络层）**

1. **人员选择**

    * 用于选择院内同事
    * 需要组织架构数据、人员数据、搜索能力

2. **组织架构选择**

    * 选择部门树
    * 多选/单选
    * 用于申请、审批等流程

3. **消息能力**

    * WebSocket / 推送
    * 消息通知
    * 消息列表、角标

4. **文件上传**

    * 图片/视频压缩、裁剪、上传
    * 附件预览
    * 断点续传
    * 后台上传

这些都属于 data-network 的典型增强模块。

---

### ✔ **core-ui 方向（UI 增强）**

5. **流程审批组件**

    * 流程图展示
    * 节点信息
    * 审批记录
    * 与表单联动

属于核心复用 UI 模块。

---

### ✔ **core-common 方向（基础能力）**

6. **App 权限模块**

    * 权限检查
    * 权限结果统一处理
    * 权限弹窗 UI
    * 与设置页面联动

为所有 App 提供统一的权限处理方案。

---

## 五、完整模块生态（推荐未来形态）

给你一份更专业的结构图（你项目未来就是这样运作的）：

```
project/
├── app-xxx/
│
├── core/
│   ├── core-common
│   ├── core-ui
│   ├── core-navigation
│
├── data/
│   ├── data-network
│   ├── data-database
│   ├── data-preferences
│   ├── data-files
│   ├── data-user
│   ├── data-org
│   ├── data-message
│
├── feature/
│   ├── login-usm
│   ├── login-atrust
│   ├── map
│   ├── webview
│   ├── employee-picker      （未来）
│   ├── dept-picker          （未来）
│   ├── approval-flow        （未来）
│
└── codegen/
```


# 资源
省市区：https://github.com/modood/Administrative-divisions-of-China?tab=readme-ov-file





我正在安卓compose平台复刻vant库的组件。我会给你一个vant组件的描述，你给我安卓代码：1. 实现对应组件，要求用法、样式等和vant组件保持一致。 2. 测试组件。编写一个compose函数，测试组件。我已经完成了一些组件的编写了。准备好了请回复“了解”。

给你一个示例参考：
package com.template.core.ui.vant

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import coil.decode.SvgDecoder

/**

VanIcon - 图标组件 (Coil SVG 版)

@param name 图标名称 (如 "location-o") 或完整 URL。

code
Code
download
content_copy
expand_less
如果是名称，默认从 assets/icons/ 目录下加载。

@param modifier 修饰符

@param size 图标大小 (默认 24dp)

@param color 图标颜色 (Tint)。如果不传，默认跟随当前文本颜色。

@param spin 是否开启旋转动画

@param rotate 图标静态旋转角度

@param directory Assets 中的子目录，默认为 "icons"

@param onClick 点击回调
*/
@Composable
fun VanIcon(
name: String,
modifier: Modifier = Modifier,
size: Dp = 24.dp, // Vant 默认大小
color: Color = Color.Unspecified,
spin: Boolean = false,
rotate: Float = 0f,
directory: String = "icons",
onClick: (() -> Unit)? = null
) {
val context = LocalContext.current

// 1. 构建 ImageLoader (支持 SVG)
// 建议在 Application 全局配置，这里为了组件独立性进行局部配置
val imageLoader = remember(context) {
ImageLoader.Builder(context)
.components {
add(SvgDecoder.Factory())
}
.build()
}

// 2. 构建 Model (路径)
val model = remember(name, directory) {
if (name.startsWith("http") || name.startsWith("file://")) {
name
} else {
// 默认加载 assets/icons/ 下的 .svg 文件
"file:///android_asset/
𝑑
𝑖
𝑟
𝑒
𝑐
𝑡
𝑜
𝑟
𝑦
/
directory/
name.svg"
}
}

// 3. 旋转动画逻辑
val currentRotate = if (spin) {
val infiniteTransition = rememberInfiniteTransition(label = "VanIconSpin")
val angle by infiniteTransition.animateFloat(
initialValue = 0f,
targetValue = 360f,
animationSpec = infiniteRepeatable(
animation = tween(1000, easing = LinearEasing),
repeatMode = RepeatMode.Restart
),
label = "VanIconSpinAngle"
)
angle
} else {
rotate
}

// 4. 点击与交互
val clickModifier = if (onClick != null) {
Modifier.clickable(
interactionSource = remember { MutableInteractionSource() },
indication = null,
onClick = onClick,
role = Role.Image
)
} else {
Modifier
}

// 5. 渲染
// 使用 Icon 组件来渲染 Painter，这样可以利用 Icon 的 tint 机制实现 SVG 变色
Box(
modifier = modifier
.size(size)
.then(clickModifier)
.rotate(currentRotate),
contentAlignment = Alignment.Center
) {
Icon(
painter = rememberAsyncImagePainter(model = model, imageLoader = imageLoader),
contentDescription = name,
// 关键：如果传入了 color，使用该 color；否则使用 LocalContentColor (跟随文本)
tint = if (color != Color.Unspecified) color else LocalContentColor.current
)
}
}

TestVan.kt

package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight

@Composable
fun DemoSection(title: String, padding: Boolean = false, content: @Composable () -> Unit) {
Column {
Text(
title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp)
)
Box(
modifier = Modifier
.fillMaxWidth()
.padding(bottom = 16.dp)
.background(Color.White)
.padding(12.dp)
) {
Column {
content()
}
}
}
}

VanBadge.kt

package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanBadge
import com.template.core.ui.vant.VanBadgePosition

class VanBadage {
}

@Composable
fun VanBadges() {
Text(
"Badge 徽标",
modifier = Modifier.padding(16.dp),
color = Color.Gray,
fontSize = 14.sp
)

code
Code
download
content_copy
expand_less
Column(
modifier = Modifier
.fillMaxWidth()
.background(Color.White)
.padding(16.dp),
verticalArrangement = Arrangement.spacedBy(24.dp) // 行间距
) {
// 1. 基础用法
Text("基础用法", fontSize = 14.sp, color = Color.Gray)
Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
VanBadge(count = 5) { BadgeChildBox() }
VanBadge(count = 10) { BadgeChildBox() }
VanBadge(content = "Hot") { BadgeChildBox() }
VanBadge(dot = true) { BadgeChildBox() }
}

    // 2. 最大值
    Text("最大值", fontSize = 14.sp, color = Color.Gray)
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        VanBadge(count = 20, max = 9) { BadgeChildBox() }
        VanBadge(count = 50, max = 20) { BadgeChildBox() }
        VanBadge(count = 200, max = 99) { BadgeChildBox() }
    }

    // 3. 自定义颜色
    Text("自定义颜色", fontSize = 14.sp, color = Color.Gray)
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        VanBadge(count = 5, color = Color(0xFF1989FA)) { BadgeChildBox() }
        VanBadge(count = 10, color = Color(0xFF1989FA)) { BadgeChildBox() }
        VanBadge(dot = true, color = Color(0xFF1989FA)) { BadgeChildBox() }
    }

    // 4. 自定义内容 (Icon)
    Text("自定义徽标内容", fontSize = 14.sp, color = Color.Gray)
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        VanBadge(badgeSlot = {
            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(10.dp))
        }) { BadgeChildBox() }

        VanBadge(badgeSlot = {
            Icon(Icons.Default.Close, null, tint = Color.White, modifier = Modifier.size(10.dp))
        }) { BadgeChildBox() }

        VanBadge(badgeSlot = {
            Icon(
                Icons.Default.KeyboardArrowDown,
                null,
                tint = Color.White,
                modifier = Modifier.size(10.dp)
            )
        }) { BadgeChildBox() }
    }

    // 5. 自定义位置
    Text("自定义位置", fontSize = 14.sp, color = Color.Gray)
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        VanBadge(count = 10, position = VanBadgePosition.TopLeft) { BadgeChildBox() }
        VanBadge(count = 10, position = VanBadgePosition.BottomLeft) { BadgeChildBox() }
        VanBadge(count = 10, position = VanBadgePosition.BottomRight) { BadgeChildBox() }
    }

    // 6. 偏移量
    Text("自定义偏移量 (x=10, y=10)", fontSize = 14.sp, color = Color.Gray)
    Row {
        VanBadge(count = 10, offset = DpOffset(10.dp, 10.dp)) { BadgeChildBox() }
    }

    // 7. 独立展示
    Text("独立展示", fontSize = 14.sp, color = Color.Gray)
    Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
        VanBadge(count = 20)
        VanBadge(count = 200, max = 99)
    }
}

}

// 模拟 Vant 文档中的灰色方块子元素
@Composable
fun BadgeChildBox() {
Box(
modifier = Modifier
.size(40.dp)
.background(Color(0xFFF2F3F5), RoundedCornerShape(4.dp))
)
}

VanButtonDemo.kt

package com.template.generated.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonIconPosition
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanButtonType

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VanButtonDemo() {
Column {
// 1. 按钮类型
DemoSection("按钮类型") {
FlowRow(
horizontalArrangement = Arrangement.spacedBy(10.dp),
verticalArrangement = Arrangement.spacedBy(10.dp)
) {
VanButton(type = VanButtonType.Primary, text = "主要按钮")
VanButton(type = VanButtonType.Success, text = "成功按钮")
VanButton(type = VanButtonType.Default, text = "默认按钮")
VanButton(type = VanButtonType.Danger, text = "危险按钮")
VanButton(type = VanButtonType.Warning, text = "警告按钮")
}
}

code
Code
download
content_copy
expand_less
// 2. 朴素按钮
DemoSection("朴素按钮") {
Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
VanButton(plain = true, type = VanButtonType.Primary, text = "朴素按钮")
VanButton(plain = true, type = VanButtonType.Success, text = "朴素按钮")
}
}

    // 3. 细边框
    DemoSection("细边框 (Hairline)") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VanButton(
                plain = true,
                hairline = true,
                type = VanButtonType.Primary,
                text = "细边框按钮"
            )
            VanButton(
                plain = true,
                hairline = true,
                type = VanButtonType.Success,
                text = "细边框按钮"
            )
        }
    }

    // 4. 禁用状态
    DemoSection("禁用状态") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VanButton(disabled = true, type = VanButtonType.Primary, text = "禁用状态")
            VanButton(disabled = true, type = VanButtonType.Success, text = "禁用状态")
        }
    }

    // 5. 加载状态
    DemoSection("加载状态") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VanButton(loading = true, type = VanButtonType.Primary)
            VanButton(
                loading = true,
                type = VanButtonType.Primary,
                loadingText = "加载中..."
            )
            VanButton(
                loading = true,
                type = VanButtonType.Success,
                loadingText = "加载中..."
            )
        }
    }

    // 6. 按钮形状
    DemoSection("按钮形状") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
            VanButton(round = true, type = VanButtonType.Success, text = "圆形按钮")
        }
    }

    // 7. 图标按钮
    DemoSection("图标按钮") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VanButton(icon = Icons.Default.Add, type = VanButtonType.Primary)
            VanButton(
                icon = Icons.Default.Add,
                type = VanButtonType.Primary,
                text = "按钮"
            )
            VanButton(
                plain = true,
                icon = Icons.Default.Star,
                type = VanButtonType.Primary,
                text = "按钮"
            )
        }
    }

    // 8. 图标位置
    DemoSection("图标位置") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            VanButton(
                icon = Icons.Default.ArrowForward,
                iconPosition = VanButtonIconPosition.Right,
                type = VanButtonType.Primary,
                text = "下一步"
            )
        }
    }

    // 9. 按钮尺寸
    DemoSection("按钮尺寸") {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            VanButton(
                size = VanButtonSize.Large,
                type = VanButtonType.Primary,
                text = "大号按钮",
                block = true
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VanButton(
                    size = VanButtonSize.Normal,
                    type = VanButtonType.Primary,
                    text = "普通按钮"
                )
                VanButton(
                    size = VanButtonSize.Small,
                    type = VanButtonType.Primary,
                    text = "小型按钮"
                )
                VanButton(
                    size = VanButtonSize.Mini,
                    type = VanButtonType.Primary,
                    text = "迷你"
                )
            }
        }
    }

    // 10. 块级元素
    DemoSection("块级元素") {
        VanButton(type = VanButtonType.Primary, block = true, text = "块级元素")
    }

    // 11. 自定义颜色
    DemoSection("自定义颜色") {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            VanButton(color = Color(0xFF7232DD), text = "单色按钮")
            VanButton(color = Color(0xFF7232DD), plain = true, text = "单色按钮")

            // 渐变色
            val gradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFFFF6034), Color(0xFFEE0A24))
            )
            VanButton(
                gradient = gradient,
                text = "渐变色按钮",
                color = Color.White // 这里指定color是为了确保文字是白色的
            )
        }
        Row {
            VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
            VanButton(square = true, type = VanButtonType.Primary, text = "方形按钮")
        }
    }
}

}

VanCellGroups.kt

package com.template.generated.vant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellArrowDirection
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanCellSize

@Composable
fun VanCellGroups() {
Column() {
// ================== 2. Cell 单元格测试 ==================

code
Code
download
content_copy
expand_less
// --- 基础用法 ---
VanCellGroup(title = "Cell 基础用法") {
VanCell(title = "单元格", value = "内容")
VanCell(title = "单元格", value = "内容", label = "描述信息", border = false)
}

    // --- 卡片风格 ---
    VanCellGroup(title = "卡片风格 (Inset)", inset = true) {
        VanCell(title = "单元格", value = "内容")
        VanCell(title = "单元格", value = "内容", label = "描述信息", border = false)
    }

    // --- 单元格大小 ---
    VanCellGroup(title = "单元格大小") {
        VanCell(title = "普通", value = "内容")
        VanCell(
            title = "大号",
            value = "内容",
            size = VanCellSize.Large,
            label = "描述信息",
            border = false
        )
    }

    // --- 展示图标 & 箭头 ---
    VanCellGroup(title = "展示图标与箭头") {
        VanCell(title = "带图标", icon = Icons.Filled.LocationOn, value = "定位")
        VanCell(title = "跳转链接", isLink = true)
        VanCell(
            title = "向下箭头",
            isLink = true,
            arrowDirection = VanCellArrowDirection.Down,
            value = "展开",
            border = false
        )
    }

    // --- 垂直居中 ---
    VanCellGroup(title = "垂直居中 (Center)") {
        VanCell(
            center = true,
            title = "多行文本",
            value = "内容居中",
            label = "这是一段很长很长的描述信息，会让单元格高度增加，此时右侧内容应该垂直居中。",
            border = false
        )
    }

    // --- 高级用法 (插槽) ---
    VanCellGroup(title = "高级用法 (自定义插槽)") {
        // 自定义右侧图标
        VanCell(
            title = "自定义右侧图标",
            rightIconComposable = {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        )

        // 自定义标题
        VanCell(
            value = "自定义标题",
            isLink = true,
            titleComposable = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("我的等级")
                    Spacer(modifier = Modifier.width(4.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.extraSmall
                    ) {
                        Text(
                            "LV.5",
                            fontSize = 10.sp,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                        )
                    }
                }
            },
            border = false
        )
    }
}

}

VanCheckboxDemo.kt

package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.template.core.ui.vant.VanCheckbox
import com.template.core.ui.vant.VanCheckboxDirection
import com.template.core.ui.vant.VanCheckboxGroup
import com.template.core.ui.vant.VanCheckboxShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VanCheckboxDemo() {

code
Code
download
content_copy
expand_less
Column(
modifier = Modifier
.fillMaxSize()
.background(Color(0xFFF7F8FA)) // Vant 背景灰
.padding(bottom = 40.dp)
) {
DemoTitle("基础用法")
BasicUsage()

    DemoTitle("自定义样式")
    CustomStyle()

    DemoTitle("异步更新")
    AsyncUpdate()

    DemoTitle("复选框组 (Vertical)")
    CheckboxGroupDemo()

    DemoTitle("复选框组 (Horizontal)")
    CheckboxGroupHorizontalDemo()

    DemoTitle("限制最大可选数 (Max = 2)")
    CheckboxGroupMaxDemo()

    DemoTitle("全选与反选")
    CheckAllDemo()

    DemoTitle("搭配单元格")
    CellIntegrationDemo()
}

}

// --- 1. 基础用法 ---
@Composable
private fun BasicUsage() {
var checked1 by remember { mutableStateOf(false) }
var checked2 by remember { mutableStateOf(true) }

code
Code
download
content_copy
expand_less
Column(
Modifier.padding(horizontal = 16.dp),
verticalArrangement = Arrangement.spacedBy(10.dp)
) {
VanCheckbox(
checked = checked1,
onChange = { checked1 = it }
) {
Text("复选框 (Status: $checked1)")
}

    VanCheckbox(
        checked = checked2,
        onChange = { checked2 = it }
    ) {
        Text("默认勾选")
    }

    VanCheckbox(checked = false, disabled = true) {
        Text("禁用复选框")
    }

    VanCheckbox(checked = true, disabled = true) {
        Text("禁用且勾选")
    }

    var checkedLabel by remember { mutableStateOf(true) }
    VanCheckbox(
        checked = checkedLabel,
        onChange = { checkedLabel = it },
        labelDisabled = true
    ) {
        Text("禁止文本点击 (只能点框)")
    }
}

}

// --- 2. 自定义样式 ---
@Composable
private fun CustomStyle() {
var checked1 by remember { mutableStateOf(true) }
var checked2 by remember { mutableStateOf(true) }
var checked3 by remember { mutableStateOf(true) }
var checked4 by remember { mutableStateOf(true) }

code
Code
download
content_copy
expand_less
Column(
Modifier.padding(horizontal = 16.dp),
verticalArrangement = Arrangement.spacedBy(10.dp)
) {
VanCheckbox(
checked = checked1,
onChange = { checked1 = it },
shape = VanCheckboxShape.Square
) {
Text("自定义形状 (Square)")
}

    VanCheckbox(
        checked = checked2,
        onChange = { checked2 = it },
        checkedColor = Color(0xFFEE0A24)
    ) {
        Text("自定义颜色 (Red)")
    }

    VanCheckbox(
        checked = checked3,
        onChange = { checked3 = it },
        iconSize = 24.dp
    ) {
        Text("自定义大小 (24dp)")
    }

    // 自定义图标 Render
    val activeIcon = "https://img.yzcdn.cn/vant/user-active.png"
    val inactiveIcon = "https://img.yzcdn.cn/vant/user-inactive.png"

    VanCheckbox(
        checked = checked4,
        onChange = { checked4 = it },
        iconRender = { checked, _ ->
            val url = if (checked) activeIcon else inactiveIcon
            AsyncImage(
                model = url,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        }
    ) {
        Text("自定义图标 (网络图)")
    }
}

}

// --- 3. 异步更新 ---
@Composable
private fun AsyncUpdate() {
var checked by remember { mutableStateOf(false) }
var loading by remember { mutableStateOf(false) }
val scope = rememberCoroutineScope()

code
Code
download
content_copy
expand_less
Column(Modifier.padding(horizontal = 16.dp)) {
VanCheckbox(
checked = checked,
onChange = { newVal ->
if (!loading) {
loading = true
// 模拟网络请求
scope.launch {
delay(500)
checked = newVal
loading = false
}
}
}
) {
Text(if (loading) "更新中..." else "复选框 (延迟500ms)")
}
}

}

// --- 4. 复选框组 ---
@Composable
private fun CheckboxGroupDemo() {
var values by remember { mutableStateOf(setOf("a", "b")) } // 默认选 a, b

code
Code
download
content_copy
expand_less
Column(Modifier.padding(horizontal = 16.dp)) {
Text(
"当前值: $values",
fontSize = 12.sp,
color = Color.Gray,
modifier = Modifier.padding(bottom = 8.dp)
)

    VanCheckboxGroup(
        value = values,
        onChange = { values = it }
    ) {
        VanCheckbox(name = "a") { Text("复选框 A") }
        VanCheckbox(name = "b") { Text("复选框 B") }
        VanCheckbox(name = "c") { Text("复选框 C") }
    }
}

}

// --- 5. 水平排列 ---
@Composable
private fun CheckboxGroupHorizontalDemo() {
var values by remember { mutableStateOf(setOf<String>()) }

code
Code
download
content_copy
expand_less
Column(Modifier.padding(horizontal = 16.dp)) {
VanCheckboxGroup(
value = values,
onChange = { values = it },
direction = VanCheckboxDirection.Horizontal
) {
VanCheckbox(name = "a") { Text("复选框 A") }
VanCheckbox(name = "b") { Text("复选框 B") }
VanCheckbox(name = "c") { Text("复选框 C") }
}
}

}

// --- 6. 限制最大可选数 ---
@Composable
private fun CheckboxGroupMaxDemo() {
var values by remember { mutableStateOf(setOf<String>()) }

code
Code
download
content_copy
expand_less
Column(Modifier.padding(horizontal = 16.dp)) {
VanCheckboxGroup(
value = values,
onChange = { values = it },
max = 2
) {
VanCheckbox(name = "a") { Text("复选框 A") }
VanCheckbox(name = "b") { Text("复选框 B") }
VanCheckbox(name = "c") { Text("复选框 C (最多选2个)") }
}
}

}

// --- 7. 全选与反选 ---
@Composable
private fun CheckAllDemo() {
val allItems = listOf("a", "b", "c")
var values by remember { mutableStateOf(setOf("a")) }

code
Code
download
content_copy
expand_less
Column(Modifier.padding(horizontal = 16.dp)) {
VanCheckboxGroup(
value = values,
onChange = { values = it }
) {
allItems.forEach { item ->
VanCheckbox(name = item) { Text("复选框 $item") }
}
}

    Row(Modifier.padding(top = 16.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        Button(onClick = { values = allItems.toSet() }) {
            Text("全选")
        }
        Button(onClick = {
            // 反选逻辑：当前有的去掉，没有的加上
            val newSet = allItems.filter { !values.contains(it) }.toSet()
            values = newSet
        }) {
            Text("反选")
        }
    }
}

}

// --- 8. 搭配单元格 ---
// 简单模拟 VanCell，实际项目中请引用真正的 Cell 组件
@Composable
private fun CellIntegrationDemo() {
var values by remember { mutableStateOf(setOf<String>()) }

code
Code
download
content_copy
expand_less
// 辅助函数：切换某个 key
fun toggle(key: String) {
val newSet = values.toMutableSet()
if (newSet.contains(key)) newSet.remove(key) else newSet.add(key)
values = newSet
}

Column(Modifier.fillMaxWidth()) {
VanCheckboxGroup(value = values, onChange = { values = it }) {
// 模拟 CellGroup
Column(Modifier.background(Color.White)) {

            // Cell 1
            MockCell(
                title = "单选框 1",
                onClick = { toggle("a") },
                rightIcon = { VanCheckbox(name = "a") }
            )
            Divider(color = Color(0xFFF5F6F7))

            // Cell 2
            MockCell(
                title = "单选框 2",
                onClick = { toggle("b") },
                rightIcon = { VanCheckbox(name = "b") }
            )
        }
    }
}

}

@Composable
fun MockCell(
title: String,
onClick: () -> Unit,
rightIcon: @Composable () -> Unit
) {
Row(
modifier = Modifier
.fillMaxWidth()
.clickable(onClick = onClick)
.padding(16.dp),
horizontalArrangement = Arrangement.SpaceBetween,
verticalAlignment = Alignment.CenterVertically
) {
Text(title, fontSize = 16.sp)
rightIcon()
}
}

@Composable
fun DemoTitle(text: String) {
Text(
text = text,
modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 10.dp),
color = Color.Gray,
fontSize = 14.sp
)
}

VanCollapses.kt

package com.template.generated.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanButtonType
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanCollapse
import com.template.core.ui.vant.VanCollapseItem

@Composable
fun VanCollapses() {
// --- 状态管理 ---
// 1. 基础用法 (多选)
var activeNames1 by remember { mutableStateOf(setOf("1")) }

code
Code
download
content_copy
expand_less
// 2. 手风琴 (单选)
var activeNames2 by remember { mutableStateOf(setOf("1")) }

// 3. 禁用状态
var activeNames3 by remember { mutableStateOf(setOf("1")) }

// 4. 全部展开/切换控制
var activeNames4 by remember { mutableStateOf(setOf<String>()) }

Column {
Text(
"Collapse 折叠面板",
modifier = Modifier.padding(16.dp),
color = Color.Gray,
fontSize = 14.sp
)

    // 1. 基础用法
    VanCellGroup(title = "基础用法") {
        VanCollapse(
            activeNames = activeNames1,
            onChange = { activeNames1 = it }
        ) {
            VanCollapseItem(title = "标题1", name = "1") {
                Text("代码是写出来给人看的，附带能在机器上运行。")
            }
            VanCollapseItem(title = "标题2", name = "2") {
                Text("技术无非就是那些开发它的人的共同灵魂。")
            }
            VanCollapseItem(title = "标题3", name = "3") {
                Text("在代码阅读过程中人们说脏话的频率是衡量代码质量的唯一标准。")
            }
        }
    }

    // 2. 手风琴
    VanCellGroup(title = "手风琴 (只能展开一个)") {
        VanCollapse(
            activeNames = activeNames2,
            onChange = { activeNames2 = it },
            accordion = true // 开启手风琴
        ) {
            VanCollapseItem(title = "标题1", name = "1") {
                Text("代码是写出来给人看的，附带能在机器上运行。")
            }
            VanCollapseItem(title = "标题2", name = "2") {
                Text("技术无非就是那些开发它的人的共同灵魂。")
            }
            VanCollapseItem(title = "标题3", name = "3") {
                Text("在代码阅读过程中人们说脏话的频率是衡量代码质量的唯一标准。")
            }
        }
    }

    // 3. 禁用状态与自定义标题
    VanCellGroup(title = "禁用与自定义标题") {
        VanCollapse(
            activeNames = activeNames3,
            onChange = { activeNames3 = it }
        ) {
            VanCollapseItem(title = "标题1", name = "1") {
                Text("正常内容")
            }
            VanCollapseItem(title = "标题2 (禁用)", name = "2", disabled = true) {
                Text("这部分内容无法点击展开")
            }
            // 自定义标题插槽
            VanCollapseItem(
                name = "3",
                titleComposable = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("标题3")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            Icons.Filled.Info,
                            null,
                            tint = Color.Blue,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            ) {
                Text("通过插槽自定义了标题内容，带了一个小图标。")
            }
        }
    }

    // 4. 外部控制 (Toggle All)
    VanCellGroup(title = "外部控制 (全部展开/切换)") {
        VanCollapse(
            activeNames = activeNames4,
            onChange = { activeNames4 = it }
        ) {
            VanCollapseItem(title = "标题1", name = "1") { Text("内容1") }
            VanCollapseItem(title = "标题2", name = "2") { Text("内容2") }
        }

        // 控制按钮区域
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            VanButton(
                type = VanButtonType.Primary,
                size = VanButtonSize.Small,
                text = "全部展开",
                onClick = { activeNames4 = setOf("1", "2") }
            )
            VanButton(
                type = VanButtonType.Default,
                size = VanButtonSize.Small,
                text = "全部收起",
                onClick = { activeNames4 = emptySet() }
            )
        }
    }
}

}

VanDialogDemo.kt

package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.template.core.ui.vant.DialogOptions
import com.template.core.ui.vant.LocalVanDialog
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanDialog
import com.template.core.ui.vant.VanDialogTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VanDialogDemo() {
val context = LocalContext.current
val dialogController = LocalVanDialog.current
val scope = rememberCoroutineScope()

code
Code
download
content_copy
expand_less
// 组件式调用的状态
var componentVisible by remember { mutableStateOf(false) }

Column(
modifier = Modifier
.fillMaxWidth()
.padding(vertical = 16.dp),
verticalArrangement = Arrangement.spacedBy(24.dp)
) {
Text(
"Dialog 弹出框",
color = Color.Gray,
fontSize = 14.sp,
modifier = Modifier.padding(horizontal = 16.dp)
)

    // 1. 函数调用 (消息提示)
    DemoSection("消息提示 (函数调用)", padding = false) {
        VanCellGroup {
            VanCell(
                title = "弹窗提示",
                isLink = true,
                onClick = {
                    scope.launch {
                        dialogController.alert(
                            title = "标题",
                            message = "代码是写出来给人看的，附带能在机器上运行"
                        )
                    }
                }
            )
            VanCell(
                title = "弹窗提示 (无标题)",
                isLink = true,
                onClick = {
                    scope.launch {
                        dialogController.alert(
                            message = "代码是写出来给人看的，附带能在机器上运行"
                        )
                    }
                }
            )
            VanCell(
                title = "确认弹框 (Confirm)",
                isLink = true,
                onClick = {
                    scope.launch {
                        try {
                            dialogController.confirm(
                                title = "标题",
                                message = "代码是写出来给人看的，附带能在机器上运行"
                            )
                            Toast.makeText(context, "Confirm", Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            )
        }
    }

    // 2. 圆角按钮风格
    DemoSection("圆角按钮风格", padding = false) {
        VanCellGroup {
            VanCell(
                title = "圆角按钮弹窗",
                isLink = true,
                onClick = {
                    scope.launch {
                        try {
                            dialogController.confirm(
                                title = "标题",
                                message = "代码是写出来给人看的，附带能在机器上运行",
                                theme = VanDialogTheme.RoundButton
                            )
                        } catch (e: Exception) {
                        }
                    }
                }
            )
        }
    }

    // 3. 异步关闭 (模拟)
    DemoSection("异步关闭", padding = false) {
        VanCellGroup {
            VanCell(
                title = "异步关闭",
                isLink = true,
                onClick = {
                    // 使用底层 show 方法来自定义逻辑
                    dialogController.show(
                        DialogOptions(
                            title = "标题",
                            message = "点击确认 1秒后关闭",
                            showCancelButton = true,
                            dismissOnAction = false, // 只有手动 dismiss 才关闭
                            onConfirm = {
                                scope.launch {
                                    delay(1000)
                                    dialogController.dismiss()
                                    Toast.makeText(context, "异步关闭成功", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            },
                            onCancel = {
                                dialogController.dismiss()
                            }
                        )
                    )
                }
            )
        }
    }

    // 4. 组件调用 (自定义内容)
    DemoSection("组件调用 (自定义内容)", padding = false) {
        VanCellGroup {
            VanCell(
                title = "组件调用",
                isLink = true,
                onClick = { componentVisible = true }
            )
        }

        // 嵌入组件调用
        VanDialog(
            visible = componentVisible,
            onDismissRequest = { componentVisible = false },
            title = "标题",
            showCancelButton = true,
            onConfirm = {
                Toast.makeText(context, "点击确认", Toast.LENGTH_SHORT).show()
                componentVisible = false
            },
            onCancel = { componentVisible = false }
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AsyncImage(
                    model = "https://img.yzcdn.cn/vant/apple-3.jpg",
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                )
                Spacer(Modifier.height(10.dp))
                Text("这是自定义的图片内容", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}

}

VanIconDemo.kt

package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanBadge
import com.template.core.ui.vant.VanIcon

@Composable
fun VanIconDemo() {
val context = LocalContext.current

code
Code
download
content_copy
expand_less
// 模拟: 实际使用时，请确保 assets/icons/ 目录下有这些 svg 文件
// 这里为了演示方便，使用了 placeholder 的网络 SVG，或者你需要手动放置文件
// 假设你放入了 'star.svg', 'chat.svg' 等到 assets/icons/ 目录

// 如果没有本地文件，这里使用一个在线 SVG 做演示 (Vite Logo)
val demoSvgUrl = "https://vitejs.dev/logo.svg"
val demoName = "PhoneO" // 假设你下载了这个文件并命名为 vite-logo.svg 放在 assets/icons/

Column(
modifier = Modifier
.fillMaxWidth()
.padding(vertical = 16.dp),
verticalArrangement = Arrangement.spacedBy(24.dp)
) {
Text(
"Icon 图标 (Coil SVG)",
color = Color.Gray,
fontSize = 14.sp,
modifier = Modifier.padding(horizontal = 16.dp)
)
Text(
"请确保 assets/icons/ 下存在对应的 .svg 文件",
fontSize = 12.sp,
color = Color.Red,
modifier = Modifier.padding(horizontal = 16.dp)
)

    // 1. 基础用法
    DemoSection("基础用法 (加载 Assets/Network)", padding = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // 加载 assets/icons/location-o.svg (你需要放入文件)
            // 这里暂时用网络图演示效果，实际请用: VanIcon(name = "location-o")
            VanIcon(name = demoSvgUrl, size = 32.dp)

            // 假设 assets/icons/like-o.svg 存在
            VanIcon(name = "PhoneO", size = 32.dp)

            // 假设 assets/icons/star-o.svg 存在
            VanIcon(name = "star-o", size = 32.dp)
            VanIcon(name = "PhoneO", size = 32.dp)
        }
    }

    // 2. 徽标提示
    DemoSection("徽标提示 (搭配 Badge)", padding = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ) {
            // 红点
            VanBadge(dot = true) {
                VanIcon(name = demoSvgUrl, size = 32.dp)
            }

            // 数字
            VanBadge(content = "99+") {
                VanIcon(name = demoSvgUrl, size = 32.dp)
            }
        }
    }

    // 3. 图标颜色
    DemoSection("图标颜色 (Tint)", padding = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // SVG 会被自动着色
            VanIcon(name = demoSvgUrl, color = Color(0xFFF44336), size = 32.dp)
            VanIcon(name = demoSvgUrl, color = Color(0xFF3F45FF), size = 32.dp)
        }
    }

    // 4. 图标大小
    DemoSection("图标大小", padding = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            VanIcon(name = demoSvgUrl, size = 20.dp)
            VanIcon(name = demoSvgUrl, size = 30.dp)
            VanIcon(name = demoSvgUrl, size = 40.dp)
        }
    }

    // 5. 图标旋转
    DemoSection("图标旋转 (Spin & Rotate)", padding = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 动画旋转
                VanIcon(name = demoSvgUrl, spin = true, size = 32.dp, color = Color(0xFF1989FA))
                Spacer(Modifier.height(4.dp))
                Text("Spin", fontSize = 12.sp, color = Color.Gray)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // 静态旋转
                VanIcon(name = demoSvgUrl, rotate = 90f, size = 32.dp)
                Spacer(Modifier.height(4.dp))
                Text("Rotate 90°", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }

    // 6. 点击事件
    DemoSection("点击事件", padding = false) {
        Row(modifier = Modifier.padding(16.dp)) {
            VanIcon(
                name = demoSvgUrl,
                size = 32.dp,
                onClick = {
                    Toast.makeText(context, "Icon Clicked!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

}

VanImageDemo.kt

package com.template.generated.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanImage
import com.template.core.ui.vant.VanImageColors
import com.template.core.ui.vant.VanImageFit

@Composable
fun VanImageDemo() {
val src = "https://img.yzcdn.cn/vant/cat.jpeg"
val errorSrc = "https://error.url/x.jpg" // 故意错误的链接

code
Code
download
content_copy
expand_less
Column(
modifier = Modifier
.fillMaxWidth()
.padding(vertical = 16.dp),
verticalArrangement = Arrangement.spacedBy(24.dp)
) {
Text(
"Image 图片",
color = Color.Gray,
fontSize = 14.sp,
modifier = Modifier.padding(horizontal = 16.dp)
)

    // 1. 基础用法
    DemoSection("基础用法", padding = false) {
        Box(Modifier.padding(16.dp)) {
            VanImage(
                src = src,
                width = 100.dp,
                height = 100.dp
            )
        }
    }

    // 2. 填充模式,删掉这部分正常
    DemoSection("填充模式", padding = false) {
        val fits = VanImageFit.entries.toTypedArray()

        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = 3
        ) {
            fits.forEach { fit ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(100.dp)
                ) {
                    VanImage(
                        src = src,
                        width = 100.dp,
                        height = 100.dp,
                        fit = fit,
                        radius = 4.dp
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(fit.name, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }

    // 3. 圆形图片
    DemoSection("圆形图片", padding = false) {
        val fits = listOf(VanImageFit.Contain, VanImageFit.Cover, VanImageFit.Fill)

        @OptIn(ExperimentalLayoutApi::class)
        FlowRow(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            maxItemsInEachRow = 3
        ) {
            fits.forEach { fit ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(100.dp)
                ) {
                    VanImage(
                        src = src,
                        width = 100.dp,
                        height = 100.dp,
                        fit = fit,
                        round = true
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(fit.name, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }

    // 4. 加载中提示
    DemoSection("加载中提示", padding = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                VanImage(
                    src = "", // 空链接或慢速链接保持 Loading 状态
                    width = 100.dp,
                    height = 100.dp,
                    showError = false // 为了演示 Loading 样式，强制不显示错误
                )
                Spacer(Modifier.height(8.dp))
                Text("默认提示", fontSize = 12.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                VanImage(
                    src = "",
                    width = 100.dp,
                    height = 100.dp,
                    showError = false,
                    // 自定义 Loading 插槽 (Spinner)
                    loadingIcon = {
                        CircularProgressIndicator(
                            color = VanImageColors.PlaceholderIcon,
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                    }
                )
                Spacer(Modifier.height(8.dp))
                Text("自定义提示", fontSize = 12.sp)
            }
        }
    }

    // 5. 加载失败提示
    DemoSection("加载失败提示", padding = false) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                VanImage(
                    src = errorSrc, // 错误链接
                    width = 100.dp,
                    height = 100.dp
                )
                Spacer(Modifier.height(8.dp))
                Text("默认提示", fontSize = 12.sp)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                VanImage(
                    src = errorSrc,
                    width = 100.dp,
                    height = 100.dp,
                    // 自定义 Error 插槽 (文字)
                    errorIcon = {
                        Text(
                            "加载失败",
                            fontSize = 14.sp,
                            color = VanImageColors.PlaceholderText
                        )
                    }
                )
                Spacer(Modifier.height(8.dp))
                Text("自定义提示", fontSize = 12.sp)
            }
        }
    }
}

}

VanInputDemo.kt

package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanButtonType
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanInput
import com.template.core.ui.vant.VanInputAlign
import com.template.core.ui.vant.VanInputType
import com.template.core.ui.vant.VanTextArea

@Composable
fun VanInputDemo() {
val context = LocalContext.current

code
Code
download
content_copy
expand_less
Column(
modifier = Modifier
.fillMaxWidth()
.padding(vertical = 16.dp),
verticalArrangement = Arrangement.spacedBy(24.dp)
) {
Text(
"Input 输入框",
color = Color.Gray,
fontSize = 14.sp,
modifier = Modifier.padding(horizontal = 16.dp)
)

    // 1. 基础用法
    DemoSection("基础用法", padding = false) {
        var text by remember { mutableStateOf("") }
        var tel by remember { mutableStateOf("") }
        var digit by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }

        VanCellGroup {
            // 文本
            VanCell(
                title = "文本",
                valueComposable = {
                    VanInput(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = "请输入文本",
                        clearable = true,
                        align = VanInputAlign.Right // Cell 中通常右对齐看起来比较整齐，或者左对齐紧跟 Label
                    )
                }
            )
            // 手机号
            VanCell(
                title = "手机号",
                valueComposable = {
                    VanInput(
                        value = tel,
                        onValueChange = { tel = it },
                        type = VanInputType.Tel,
                        placeholder = "请输入手机号",
                        align = VanInputAlign.Right
                    )
                }
            )
            // 整数
            VanCell(
                title = "整数",
                valueComposable = {
                    VanInput(
                        value = digit,
                        onValueChange = { digit = it },
                        type = VanInputType.Digit,
                        placeholder = "请输入整数",
                        align = VanInputAlign.Right
                    )
                }
            )
            // 密码
            VanCell(
                title = "密码",
                valueComposable = {
                    VanInput(
                        value = password,
                        onValueChange = { password = it },
                        type = VanInputType.Password,
                        placeholder = "请输入密码",
                        align = VanInputAlign.Right
                    )
                }
            )
        }
    }

    // 2. 插入内容 (前后缀)
    DemoSection("插入内容", padding = false) {
        var sms by remember { mutableStateOf("") }

        VanCellGroup {
            VanCell(
                title = "短信验证码",
                valueComposable = {
                    VanInput(
                        value = sms,
                        onValueChange = { sms = it },
                        placeholder = "请输入验证码",
                        prefix = { Text("💁", fontSize = 16.sp) }, // 前缀 Emoji
                        suffix = {
                            VanButton(
                                text = "发送",
                                type = VanButtonType.Primary,
                                size = VanButtonSize.Mini,
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "发送验证码",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            )
                        }
                    )
                }
            )
        }
    }

    // 3. 多行输入
    DemoSection("多行输入 (TextArea)", padding = false) {
        var val1 by remember { mutableStateOf("") }
        var val2 by remember { mutableStateOf("") }

        VanCellGroup {
            VanCell(
                title = "多行输入",
                label = "自适应高度", // 放在 label 显示描述
                valueComposable = {
                    VanTextArea(
                        value = val1,
                        onValueChange = { val1 = it },
                        placeholder = "请输入留言"
                    )
                }
            )

            VanCell(
                title = "固定高度",
                valueComposable = {
                    VanTextArea(
                        value = val2,
                        onValueChange = { val2 = it },
                        minHeight = 100.dp, // 强制最小高度
                        placeholder = "最小高度 100dp"
                    )
                }
            )
        }
    }

    // 4. 字数统计
    DemoSection("字数统计", padding = false) {
        var val1 by remember { mutableStateOf("") }
        var val2 by remember { mutableStateOf("") }

        VanCellGroup {
            VanCell(
                title = "单行限制",
                valueComposable = {
                    VanInput(
                        value = val1,
                        onValueChange = { val1 = it },
                        maxLength = 10,
                        placeholder = "最多10个字符",
                        onOverlimit = {
                            Toast.makeText(
                                context,
                                "不能超过10个字符",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            )

            VanCell(
                title = "多行统计",
                valueComposable = {
                    VanTextArea(
                        value = val2,
                        onValueChange = { val2 = it },
                        maxLength = 50,
                        showWordLimit = true,
                        placeholder = "显示字数统计"
                    )
                }
            )
        }
    }

    // 5. 对齐方式 & 状态
    DemoSection("状态与对齐", padding = false) {
        var v1 by remember { mutableStateOf("只读模式") }
        var v2 by remember { mutableStateOf("禁用模式") }
        var v3 by remember { mutableStateOf("") }

        VanCellGroup {
            VanCell(
                title = "只读",
                valueComposable = {
                    VanInput(value = v1, onValueChange = {}, readOnly = true)
                }
            )
            VanCell(
                title = "禁用",
                valueComposable = {
                    VanInput(value = v2, onValueChange = {}, disabled = true)
                }
            )
            VanCell(
                title = "居中对齐",
                valueComposable = {
                    VanInput(
                        value = v3,
                        onValueChange = { v3 = it },
                        align = VanInputAlign.Center,
                        placeholder = "输入内容居中"
                    )
                }
            )
        }
    }
}

}

VanPopupDemo.kt

package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanPopup
import com.template.core.ui.vant.VanPopupPosition

@Composable
fun VanPopupDemo() {
// 状态管理
var showBasic by remember { mutableStateOf(false) }

code
Code
download
content_copy
expand_less
var showTop by remember { mutableStateOf(false) }
var showBottom by remember { mutableStateOf(false) }
var showLeft by remember { mutableStateOf(false) }
var showRight by remember { mutableStateOf(false) }

var showRound by remember { mutableStateOf(false) }
var showCloseable by remember { mutableStateOf(false) }
var showTitle by remember { mutableStateOf(false) }

// 由于 VanPopup 需要覆盖在页面最上层，我们这里用一个 Box 包裹整个演示页面
// 实际项目中，VanPopup 建议放在 Scaffold 的最外层 Box 中
Box(modifier = Modifier.fillMaxSize()) {
Column(
modifier = Modifier
.fillMaxWidth()
.verticalScroll(rememberScrollState()),
verticalArrangement = Arrangement.spacedBy(24.dp)
) {
Text(
"Popup 弹出层",
color = Color.Gray,
fontSize = 14.sp,
modifier = Modifier.padding(horizontal = 16.dp)
)

        // 1. 基础用法
        DemoSection("基础用法", padding = false) {
            VanCellGroup {
                VanCell(title = "展示弹出层", isLink = true, onClick = { showBasic = true })
            }
        }

//            // 1. 基础用法
//            DemoSection("子组件", padding = false) {
//
//                var showTopSub by remember { mutableStateOf(false) }
//                var showBottomSub by remember { mutableStateOf(false) }
//                VanCellGroup {
//                    VanCell(title = "展示弹出层", isLink = true, onClick = { showTopSub = true })
//                    VanCell(title = "展示弹出层", isLink = true, onClick = { showBottomSub = true })
//                }
//
//                Box(modifier = Modifier.fillMaxWidth().height(50.dp).background(Color.Red)){
//                    // 2. Top
//                    VanPopup(
//                        visible = showTopSub,
//                        onClose = { showTopSub = false },
//                        position = VanPopupPosition.Top,
//                        contentHeight = 200.dp
//                    ) {
//                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("顶部弹出") }
//                    }
//
//                    // Bottom
//                    VanPopup(
//                        visible = showBottomSub,
//                        onClose = { showBottomSub = false },
//                        position = VanPopupPosition.Bottom,
//                        contentHeight = 200.dp,
//                        safeAreaInsetBottom = true
//                    ) {
//                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("底部弹出") }
//                    }
//                }
//            }

code
Code
download
content_copy
expand_less
// 2. 弹出位置
DemoSection("弹出位置", padding = false) {
VanCellGroup {
VanCell(title = "顶部弹出", isLink = true, onClick = { showTop = true })
VanCell(title = "底部弹出", isLink = true, onClick = { showBottom = true })
VanCell(title = "左侧弹出", isLink = true, onClick = { showLeft = true })
VanCell(title = "右侧弹出", isLink = true, onClick = { showRight = true })
}
}

        // 3. 圆角弹窗
        DemoSection("圆角弹窗", padding = false) {
            VanCellGroup {
                VanCell(title = "圆角弹窗", isLink = true, onClick = { showRound = true })
            }
        }

        // 4. 关闭图标
        DemoSection("关闭图标", padding = false) {
            VanCellGroup {
                VanCell(title = "关闭图标", isLink = true, onClick = { showCloseable = true })
            }
        }

        // 5. 标题弹窗
        DemoSection("标题弹窗", padding = false) {
            VanCellGroup {
                VanCell(title = "标题弹窗", isLink = true, onClick = { showTitle = true })
            }
        }
    }

    // --- Popups (放在最上层) ---

    // 1. 基础 (Center)
    VanPopup(
        visible = showBasic,
        onClose = { showBasic = false },
        contentWidth = 200.dp,
        contentHeight = 150.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("内容")
        }
    }

    // 2. Top
    VanPopup(
        visible = showTop,
        onClose = { showTop = false },
        position = VanPopupPosition.Top,
        contentHeight = 200.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("顶部弹出") }
    }

    // Bottom
    VanPopup(
        visible = showBottom,
        onClose = { showBottom = false },
        position = VanPopupPosition.Bottom,
        contentHeight = 200.dp,
        safeAreaInsetBottom = true
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("底部弹出") }
    }

    // Left
    VanPopup(
        visible = showLeft,
        onClose = { showLeft = false },
        position = VanPopupPosition.Left,
        contentWidth = 200.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("左侧弹出") }
    }

    // Right
    VanPopup(
        visible = showRight,
        onClose = { showRight = false },
        position = VanPopupPosition.Right,
        contentWidth = 200.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("右侧弹出") }
    }

    // Round
    VanPopup(
        visible = showRound,
        onClose = { showRound = false },
        position = VanPopupPosition.Bottom,
        round = true,
        contentHeight = 200.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("圆角弹窗") }
    }

    // Closeable
    VanPopup(
        visible = showCloseable,
        onClose = { showCloseable = false },
        position = VanPopupPosition.Bottom,
        closeable = true,
        contentHeight = 200.dp
    ) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("关闭图标") }
    }

    // Title
    VanPopup(
        visible = showTitle,
        onClose = { showTitle = false },
        position = VanPopupPosition.Bottom,
        round = true,
        closeable = true,
        title = "标题",
        description = "这是一段很长很长的描述这是一段很长很长的描述",
        contentHeight = 250.dp
    ) {
        // 内容区域
    }
}

}

VanRadioDemo.kt

package com.template.generated.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanCellGroup
import com.template.core.ui.vant.VanRadio
import com.template.core.ui.vant.VanRadioDirection
import com.template.core.ui.vant.VanRadioGroup
import com.template.core.ui.vant.VanRadioShape
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VanRadioDemo() {
// 外层容器，不包含滚动，滚动由父级 TestVan 提供
Column(
modifier = Modifier
.fillMaxWidth()
.padding(16.dp),
verticalArrangement = Arrangement.spacedBy(24.dp)
) {
Text("Radio 单选框", color = Color.Gray, fontSize = 14.sp)

code
Code
download
content_copy
expand_less
// 1. 基础用法
DemoSection("基础用法") {
var value by remember { mutableStateOf("1") }
VanRadioGroup(value = value, onChange = { value = it }) {
VanRadio(name = "1") { Text("单选框 1") }
VanRadio(name = "2") { Text("单选框 2") }
}
}

    // 2. 水平排列
    DemoSection("水平排列") {
        var value by remember { mutableStateOf("1") }
        VanRadioGroup(
            value = value,
            onChange = { value = it },
            direction = VanRadioDirection.Horizontal
        ) {
            VanRadio(name = "1") { Text("单选框 1") }
            VanRadio(name = "2") { Text("单选框 2") }
        }
    }

    // 3. 禁用状态
    DemoSection("禁用状态") {
        var value by remember { mutableStateOf("1") }
        VanRadioGroup(value = value, onChange = { value = it }) {
            VanRadio(name = "1", disabled = true) { Text("单选框 1 (Disabled)") }
            VanRadio(name = "2", disabled = true) { Text("单选框 2 (Disabled)") }
        }
    }

    // 4. 自定义形状 (Square)
    DemoSection("自定义形状 (Square)") {
        var value by remember { mutableStateOf("1") }
        VanRadioGroup(value = value, onChange = { value = it }) {
            VanRadio(name = "1", shape = VanRadioShape.Square) { Text("单选框 1") }
            VanRadio(name = "2", shape = VanRadioShape.Square) { Text("单选框 2") }
        }
    }

    // 5. 自定义颜色
    DemoSection("自定义颜色") {
        var value by remember { mutableStateOf("1") }
        VanRadioGroup(value = value, onChange = { value = it }) {
            VanRadio(name = "1", checkedColor = Color(0xFFEE0A24)) { Text("单选框 1") }
            VanRadio(name = "2", checkedColor = Color(0xFFEE0A24)) { Text("单选框 2") }
        }
    }

    // 6. 自定义大小
    DemoSection("自定义大小") {
        var value by remember { mutableStateOf("1") }
        VanRadioGroup(value = value, onChange = { value = it }) {
            VanRadio(name = "1", iconSize = 24.dp) { Text("单选框 1 (24dp)") }
            VanRadio(name = "2", iconSize = 24.dp) { Text("单选框 2 (24dp)") }
        }
    }

    // 7. 禁用文本点击
    DemoSection("禁用文本点击") {
        var value by remember { mutableStateOf("1") }
        VanRadioGroup(value = value, onChange = { value = it }) {
            VanRadio(name = "1", labelDisabled = true) { Text("单选框 1 (只能点图标)") }
            VanRadio(name = "2", labelDisabled = true) { Text("单选框 2 (只能点图标)") }
        }
    }

    // 8. 异步更新
    DemoSection("异步更新") {
        var value by remember { mutableStateOf("1") }
        var loading by remember { mutableStateOf(false) }
        val scope = rememberCoroutineScope()

        VanRadioGroup(
            value = value,
            onChange = { newValue ->
                if (!loading && newValue != value) {
                    loading = true
                    scope.launch {
                        delay(500) // 模拟网络请求
                        value = newValue
                        loading = false
                    }
                }
            }
        ) {
            VanRadio(name = "1") { Text("单选框 1") }
            VanRadio(name = "2") { Text("单选框 2") }
        }
        if (loading) {
            Row(Modifier.padding(top = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                Spacer(Modifier.width(8.dp))
                Text("更新中...", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }

    // 9. 搭配单元格组件
    DemoSection("搭配单元格组件") {
        var cellValue by remember { mutableStateOf("1") }

        // 使用 VanCellGroup 和 VanCell
        // 注意：VanCell 需要处理 onClick 来更新 Radio 的状态
        VanRadioGroup(value = cellValue, onChange = { cellValue = it }) {
            VanCellGroup {
                VanCell(
                    title = "单选框 1",
                    clickable = true,
                    onClick = { cellValue = "1" }, // Cell 点击触发更新
                    rightIconComposable = {
                        VanRadio(name = "1") // 这里的 VanRadio 只负责显示状态
                    }
                )
                VanCell(
                    title = "单选框 2",
                    clickable = true,
                    onClick = { cellValue = "2" },
                    rightIconComposable = {
                        VanRadio(name = "2")
                    }
                )
            }
        }
    }
}

}

VanSearchDemo.kt

package com.template.generated.vant

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanSearch
import com.template.core.ui.vant.VanSearchAlign
import com.template.core.ui.vant.VanSearchShape

@Composable
fun VanSearchDemo() {
val context = LocalContext.current

code
Code
download
content_copy
expand_less
Column(
modifier = Modifier
.fillMaxWidth()
.padding(bottom = 20.dp),
verticalArrangement = Arrangement.spacedBy(20.dp)
) {
Text(
"Search 搜索",
color = Color.Gray,
fontSize = 14.sp,
modifier = Modifier.padding(start = 16.dp, top = 16.dp)
)

    // 1. 基础用法
    DemoSection("基础用法", padding = false) {
        var value by remember { mutableStateOf("") }
        VanSearch(
            value = value,
            onValueChange = { value = it },
            placeholder = "请输入搜索关键词"
        )
    }

    // 2. 事件监听
    DemoSection("事件监听", padding = false) {
        var value by remember { mutableStateOf("") }
        VanSearch(
            value = value,
            onValueChange = { value = it },
            showAction = true, // 显示右侧取消按钮
            placeholder = "请输入搜索关键词",
            onSearch = {
                Toast.makeText(context, "Search: $it", Toast.LENGTH_SHORT).show()
            },
            onCancel = {
                Toast.makeText(context, "Cancel", Toast.LENGTH_SHORT).show()
                value = ""
            },
            onClear = {
                Toast.makeText(context, "Clear", Toast.LENGTH_SHORT).show()
            },
            onClickInput = {
                // Toast.makeText(context, "Click Input", Toast.LENGTH_SHORT).show()
            }
        )
    }

    // 3. 搜索框内容对齐 (Center)
    DemoSection("搜索框内容对齐 (Center)", padding = false) {
        var value by remember { mutableStateOf("") }
        VanSearch(
            value = value,
            onValueChange = { value = it },
            align = VanSearchAlign.Center,
            placeholder = "请输入搜索关键词"
        )
    }

    // 4. 禁用搜索框
    DemoSection("禁用搜索框", padding = false) {
        VanSearch(
            value = "无法输入",
            onValueChange = {},
            disabled = true,
            placeholder = "请输入搜索关键词"
        )
    }

    // 5. 自定义背景色 & 圆角
    DemoSection("自定义背景色 & 圆角", padding = false) {
        var value by remember { mutableStateOf("") }
        VanSearch(
            value = value,
            onValueChange = { value = it },
            shape = VanSearchShape.Round,
            background = Color(0xFF4FC08D),
            placeholder = "请输入搜索关键词"
        )
    }

    // 6. 自定义按钮
    DemoSection("自定义按钮 (Action)", padding = false) {
        var value by remember { mutableStateOf("") }
        VanSearch(
            value = value,
            onValueChange = { value = it },
            label = { Text("地址") }, // 左侧 Label
            placeholder = "请输入搜索关键词",
            action = {
                // 自定义 Action 插槽
                Text(
                    text = "搜索",
                    color = Color(0xFF323233),
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Custom Search: $value", Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
        )
    }
}

}

VanSliders.kt

package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanSlider

@Composable
fun VanSliders() {
// 状态
var value1 by remember { mutableFloatStateOf(50f) }
var valueRange by remember { mutableStateOf(listOf(20f, 60f)) }
var valueStep by remember { mutableFloatStateOf(0f) }
var valueVertical by remember { mutableFloatStateOf(50f) }
var valueCustom by remember { mutableFloatStateOf(30f) }

code
Code
download
content_copy
expand_less
Column {
Text(
"Slider 滑块",
modifier = Modifier.padding(16.dp),
color = Color.Gray,
fontSize = 14.sp
)

    // 1. 基础用法
    Text(
        "基础用法: ${value1.toInt()}",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
    Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
        VanSlider(
            value = value1,
            onValueChange = { value1 = it as Float }
        )
    }

    // 2. 双滑块
    Text(
        "双滑块: ${valueRange[0].toInt()} - ${valueRange[1].toInt()}",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
    Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
        VanSlider(
            range = true,
            value = valueRange,
            onValueChange = { valueRange = it as List<Float> }
        )
    }

    // 3. 指定步长
    Text(
        "指定步长 (Step=10): ${valueStep.toInt()}",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
    Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
        VanSlider(
            step = 10f,
            value = valueStep,
            onValueChange = { valueStep = it as Float }
        )
    }

    // 4. 自定义样式 & 按钮
    Text(
        "自定义样式 & 按钮",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
    Box(modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)) {
        VanSlider(
            value = valueCustom,
            activeColor = Color(0xFFEE0A24),
            barHeight = 4.dp,
            onValueChange = { valueCustom = it as Float },
            button = {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFEE0A24), RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "${valueCustom.toInt()}",
                        color = Color.White,
                        fontSize = 10.sp
                    )
                }
            }
        )
    }

    // 5. 垂直方向
    Text(
        "垂直方向",
        fontSize = 14.sp,
        color = Color.Gray,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
    Row(
        modifier = Modifier
            .height(200.dp) // 必须给高度
            .padding(start = 30.dp, bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(40.dp)
    ) {
        VanSlider(
            vertical = true,
            value = valueVertical,
            onValueChange = { valueVertical = it as Float }
        )

        // 垂直双滑块
        VanSlider(
            vertical = true,
            range = true,
            value = valueRange, // 复用之前的 Range 值
            onValueChange = { valueRange = it as List<Float> }
        )
    }
}

}

VanSwipeCellDemo.kt

package com.template.generated.vant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.template.core.ui.vant.VanButton
import com.template.core.ui.vant.VanButtonSize
import com.template.core.ui.vant.VanCell
import com.template.core.ui.vant.VanSwipeCell
import com.template.core.ui.vant.VanSwipeCellPosition
import com.template.core.ui.vant.VanSwipeCellSide
import com.template.core.ui.vant.rememberVanSwipeCellState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VanSwipeCellDemo() {
Column(
modifier = Modifier
.fillMaxWidth()
.padding(vertical = 16.dp),
verticalArrangement = Arrangement.spacedBy(24.dp)
) {
Text(
"SwipeCell 滑动单元格",
color = Color.Gray,
fontSize = 14.sp,
modifier = Modifier.padding(horizontal = 16.dp)
)

code
Code
download
content_copy
expand_less
// 1. 基础用法
DemoSection("基础用法", padding = false) {
VanSwipeCell(
rightAction = {
// Action 按钮高度会自动撑满
ActionBox(color = Color(0xFFEE0A24), text = "删除")
},
onOpen = { /* log */ },
onClose = { /* log */ }
) {
VanCell(title = "单元格", value = "内容")
}
}

    // 2. 事件监听与双侧滑动
    DemoSection("事件监听", padding = false) {
        VanSwipeCell(
            leftAction = {
                ActionBox(color = Color(0xFF1989FA), text = "选择")
            },
            rightAction = {
                Row {
                    ActionBox(color = Color(0xFFEE0A24), text = "删除")
                    ActionBox(color = Color(0xFF1989FA), text = "收藏")
                }
            },
            onClick = { position ->
                // Handle click: Left, Right, Cell
            }
        ) {
            VanCell(title = "单元格", value = "左右均可滑动")
        }
    }

    // 3. 自定义内容 (商品卡片)
    DemoSection("自定义内容", padding = false) {
        VanSwipeCell(
            rightAction = {
                ActionBox(color = Color(0xFFEE0A24), text = "删除")
            }
        ) {
            // 模拟商品卡片布局
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White) // 显式背景
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

//                    Image(
//                        painter = rememberAsyncImagePainter("https://img.yzcdn.cn/vant/ipad.jpeg"),
//                        contentDescription = null,
//                        modifier = Modifier.size(88.dp).background(Color.LightGray),
//                        contentScale = ContentScale.Crop
//                    )
AsyncImage(
model = "https://img.yzcdn.cn/vant/ipad.jpeg",
contentDescription = null,
modifier = Modifier
.size(88.dp)
.background(Color.LightGray),
contentScale = ContentScale.Crop
)
Spacer(modifier = Modifier.width(16.dp))
Column(
modifier = Modifier
.weight(1f)
.height(88.dp),
verticalArrangement = Arrangement.SpaceBetween
) {
Column {
Text("商品标题", fontWeight = FontWeight.Bold, fontSize = 16.sp)
Text("这里是商品描述", color = Color.Gray, fontSize = 12.sp)
}
Row(
modifier = Modifier.fillMaxWidth(),
horizontalArrangement = Arrangement.SpaceBetween
) {
Text("¥2.00", fontWeight = FontWeight.Bold, fontSize = 16.sp)
Text("x2", color = Color.Gray, fontSize = 12.sp)
}
}
}
}
}
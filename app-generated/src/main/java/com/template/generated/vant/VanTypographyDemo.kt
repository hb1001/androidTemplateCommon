package com.template.generated.vant

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.template.core.ui.vant.VanEllipsisConfig
import com.template.core.ui.vant.VanLink
import com.template.core.ui.vant.VanTitle
import com.template.core.ui.vant.VanTypography
import com.template.core.ui.vant.VanTypographyColors
import com.template.core.ui.vant.VanTypographyLevel
import com.template.core.ui.vant.VanTypographyType

@Composable
fun VanTypographyDemo() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text("Typography 文本", color = Color.Gray, fontSize = 14.sp)

        // 1. 基础用法
        DemoSection("基础用法 (AnnotatedString)") {
            // 模拟 React Vant 的嵌套结构：
            // In the process of <Text type="danger">internal</Text> ...
            val text = buildAnnotatedString {
                append("In the process of ")
                withStyle(SpanStyle(color = VanTypographyColors.Danger)) {
                    append("internal")
                }
                append(" ")
                withStyle(SpanStyle(textDecoration = TextDecoration.LineThrough)) {
                    append("desktop")
                }
                append(" applications development, ")
                withStyle(SpanStyle(color = VanTypographyColors.Primary)) {
                    append("many different")
                }
                append(" design specs and ")
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append("implementations")
                }
                append(" would be ")
                withStyle(SpanStyle(color = VanTypographyColors.Warning)) {
                    append("involved")
                }
            }
            VanTypography(text = text)
        }

        // 2. 文本省略
        val content =
            "React Vant 是一套轻量、可靠的移动端 React 组件库，提供了丰富的基础组件和业务组件，帮助开发者快速搭建移动应用，使用过程中发现任何问题都可以提 Issue 给我们，当然，我们也非常欢迎你给我们发 PR。"

        DemoSection("文本省略 (Ellipsis)") {
            // 单行省略
            Text("单行省略:", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(rows = 1)
            )
            Divider(Modifier.padding(vertical = 8.dp))

            // 多行省略
            Text("多行省略 (Rows=2):", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(rows = 2)
            )
            Divider(Modifier.padding(vertical = 8.dp))

            // 带展开/收起
            Text("带展开/收起:", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(
                    rows = 2,
                    collapseText = "收起",
                    expandText = "展开"
                )
            )
            Divider(Modifier.padding(vertical = 8.dp))

            // 保留末位文本 (如文件名)
            Text("保留末位文本 (中间省略):", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(
                    rows = 2,
                    symbol = "......",
                    suffixCount = 10 // 保留最后10个字
                )
            )
            Divider(Modifier.padding(vertical = 8.dp))

            // 自定义后缀
            Text("自定义文本后缀:", fontSize = 12.sp, color = Color.Gray)
            VanTypography(
                text = content,
                ellipsis = VanEllipsisConfig(
                    rows = 2,
                    suffixText = "--William",
                    expandText = "更多"
                )
            )
        }

        // 3. 标题
        DemoSection("标题 (Title)") {
            VanTitle(text = "一级测试标题 (Level 1)", level = VanTypographyLevel.L1)
            VanTitle(text = "二级测试标题 (Level 2)", level = VanTypographyLevel.L2)
            VanTitle(text = "三级测试标题 (Level 3)", level = VanTypographyLevel.L3)
            VanTitle(text = "四级测试标题 (Level 4)", level = VanTypographyLevel.L4)
            VanTitle(text = "五级测试标题 (Level 5)", level = VanTypographyLevel.L5)
            VanTitle(text = "六级测试标题 (Level 6)", level = VanTypographyLevel.L6)
        }

        // 4. 链接
        DemoSection("链接 (Link)") {
            VanLink(
                text = "测试 Link (Open Google)",
                url = "https://www.google.com"
            )
            Spacer(Modifier.height(8.dp))
            VanLink(
                text = "测试 Link (Underline)",
                underline = true,
                onClick = { /* Handle click */ }
            )
        }

        // 5. 样式变量模拟
        DemoSection("类型样式 (Types)") {
            VanTypography(text = "Default Type")
            VanTypography(text = "Primary Type", type = VanTypographyType.Primary)
            VanTypography(text = "Success Type", type = VanTypographyType.Success)
            VanTypography(text = "Danger Type", type = VanTypographyType.Danger)
            VanTypography(text = "Warning Type", type = VanTypographyType.Warning)
            VanTypography(text = "Secondary Type", type = VanTypographyType.Secondary)
            VanTypography(text = "Disabled Text", disabled = true)
        }
    }
}
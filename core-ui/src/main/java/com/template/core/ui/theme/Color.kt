// File: core-ui/src/main/java/com/template/core/ui/theme/Color.kt
package com.template.core.ui.theme

import androidx.compose.ui.graphics.Color

// 1. 基础品牌色
val BrandBlue = Color(0xFF038AFE)

// 2. 浅蓝色 (用于 InputChip 选中背景)
// 基于 BrandBlue 调淡的颜色，看起来像淡蓝
val BrandLightBlue = Color(0xFFE6F4FF)

// 3. 灰色系
val PageBackground = Color(0xFFFFFFFF) // 你要求的页面背景白
val CardGray = Color(0xFFF5F5F5)       // 新增：卡片背景灰 (比白色稍微灰一点)
val TextGray = Color(0xFF666666)       // 辅助文字颜色
val TextBlack = Color(0xFF222222)      // 主要文字颜色

// 4. 红色系
val ErrorRed = Color(0xFFFF4D4F)       // 鲜艳的红
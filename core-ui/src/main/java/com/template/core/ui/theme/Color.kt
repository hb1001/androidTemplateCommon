// File: core-ui/src/main/java/com/template/core/ui/theme/Color.kt
package com.template.core.ui.theme

import androidx.compose.ui.graphics.Color

// 1. 基础品牌色
val BrandBlue = Color(0xFF038AFE)

// 2. 浅蓝色 (用于 InputChip 选中背景)
// 基于 BrandBlue 调淡的颜色，看起来像淡蓝
val BrandLightBlue = Color(0xFFE6F4FF)

// 3. 灰色系
val PageBackground = Color(0xFFFaFaFa) // 页面背景
val CardGray = Color(0xFFF5F5F5)       // 新增：卡片背景灰 (比白色稍微灰一点)
val TextGray = Color(0xFF666666)       // 辅助文字颜色
val TextBlack = Color(0xFF222222)      // 主要文字颜色

// 4. 红色系
val ErrorRed = Color(0xFFFF4D4F)       // 鲜艳的红


/**
 *
 *
 * 你特别关心的 surfaceVariant 在这里对应的是灰色的卡片背景。
 * 1. 背景与卡片 (Surface & Background)
 * 这些是你布局中最常用的底层颜色。
 * 颜色角色 (Role)	对应变量	Hex 值	视觉效果	典型用途
 * background	PageBackground	#FFFFFF (纯白)	⬜ 白色	整个页面的大背景
 * onBackground	TextBlack	#222222 (几近黑)	⬛ 深黑	页面背景上的文字
 * surface	Color.White	#FFFFFF (纯白)	⬜ 白色	TopAppBar, BottomSheet, 标准卡片
 * onSurface	TextBlack	#222222 (几近黑)	⬛ 深黑	标准卡片上的主标题文字
 * surfaceVariant	CardGray	#F5F5F5 (浅灰)	🌫 浅灰	次级卡片背景、分割线
 * onSurfaceVariant	TextGray	#666666 (深灰)	👽 深灰	辅助文字、次要信息、图标
 * 2. 主色调 (Primary)
 * 用于强调、按钮、激活状态。
 * 颜色角色 (Role)	对应变量	Hex 值	视觉效果	典型用途
 * primary	BrandBlue	#038AFE (品牌蓝)	🔵 亮蓝	按钮背景、Switch激活、光标
 * onPrimary	Color.White	#FFFFFF (纯白)	⬜ 白色	按钮上的文字
 * primaryContainer	BrandLightBlue	#E6F4FF (淡蓝)	💧 淡蓝	FAB 背景、选中状态的高亮底色
 * onPrimaryContainer	BrandBlue	#038AFE (品牌蓝)	🔵 亮蓝	淡蓝底色上的文字/图标
 * 3. 次要色调 (Secondary)
 * 注意：在你的配置中，Secondary 和 Primary 设置完全一样。
 * 颜色角色 (Role)	对应变量	Hex 值	视觉效果	典型用途
 * secondary	BrandBlue	#038AFE	🔵 亮蓝	(同 Primary)
 * secondaryContainer	BrandLightBlue	#E6F4FF	💧 淡蓝	InputChip / FilterChip 的选中背景
 * onSecondaryContainer	BrandBlue	#038AFE	🔵 亮蓝	Chip 选中时的文字颜色
 * 4. 错误警示 (Error)
 * 注意：你修改了 errorContainer，使其变成了实心深红，而不是默认的浅红。
 * 颜色角色 (Role)	对应变量	Hex 值	视觉效果	典型用途
 * error	ErrorRed	#FFFF4D4F	🔴 鲜红	输入框报错边框
 * onError	Color.White	#FFFFFF	⬜ 白色	红色背景上的文字
 * errorContainer	ErrorRed	#FFFF4D4F	🔴 鲜红	(自定义) 退出登录按钮背景
 * onErrorContainer	Color.White	#FFFFFF	⬜ 白色	(自定义) 退出登录按钮文字
 * 总结一下怎么用：
 * 想写主要文字： MaterialTheme.colorScheme.onSurface (黑色)
 * 想写次要文字/说明文字： MaterialTheme.colorScheme.onSurfaceVariant (灰色 #666666)
 * 想用那个灰色卡片背景： MaterialTheme.colorScheme.surfaceVariant (浅灰 #F5F5F5)
 * 想用淡蓝色高亮背景： MaterialTheme.colorScheme.primaryContainer 或 secondaryContainer (淡蓝 #E6F4FF)
 *
 */
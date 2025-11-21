// File: core-ui/src/main/java/com/template/core/ui/theme/Theme.kt
package com.template.core.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    // --- 1. 主色 (Button 背景, Switch 激活) ---
    primary = BrandBlue,
    onPrimary = Color.White,

    // --- [新增] 主容器色 (FAB, 底部导航选中背景) ---
    // 建议复用浅蓝色，或者定义一个新的
    primaryContainer = BrandLightBlue,
    onPrimaryContainer = BrandBlue, // 浅蓝背景上的文字颜色

    // --- 2. 次要色 (InputChip 选中背景) ---
    secondary = BrandBlue,
    onSecondary = Color.White,
    // 这里复用浅蓝色，保证 Chip 选中是浅蓝背景
    secondaryContainer = BrandLightBlue,
    onSecondaryContainer = BrandBlue,

    // --- 3. 背景与卡片 ---
    background = PageBackground,
    onBackground = TextBlack,
    surface = Color.White,
//    surface = PageBackground,
    onSurface = TextBlack,

    // Card 想要的灰色背景
    surfaceVariant = CardGray,
    onSurfaceVariant = TextGray,

    // --- 4. 错误色 (退出按钮) ---
    // 这里的修改是为了配合你代码中写死的 errorContainer
    errorContainer = ErrorRed,      // 强行设为深红
    onErrorContainer = Color.White, // 强行设为白色文字

    error = ErrorRed,
    onError = Color.White
)

// 暗色模式简单配置（保持现状或根据需求调整）
private val DarkColorScheme = darkColorScheme(
    primary = BrandBlue,
    secondaryContainer = Color(0xFF004B7F), // 暗色模式下的 Chip 背景
    onSecondaryContainer = Color(0xFFD1E4FF),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFFCCCCCC),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6)
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // 强制关闭动态取色，确保使用我们在上面定义的 BrandBlue
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) DarkColorScheme else LightColorScheme // 逻辑保留，但参数已设为 false
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
//            window.statusBarColor = colorScheme.background.toArgb()
//            // 状态栏图标颜色自适应
//            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme

            //
            // [关键修改 1] 设置状态栏为透明，这样首页地图才能透上来
            window.statusBarColor = Color.Transparent.toArgb()

            // [关键修改 2] 开启 Edge-to-Edge (让内容延伸到状态栏背后)
            WindowCompat.setDecorFitsSystemWindows(window, false)

            // 默认状态栏图标颜色（深色模式下文字变白，浅色模式下文字变黑）
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
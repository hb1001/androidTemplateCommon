package com.template.feature.onboarding.components

import com.template.feature.onboarding.R

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// 1. 定义数据模型 (通用性)
data class OnboardingPageData(
    val title: String,
    val description: String,
    // 这里用 Color 模拟图片的主色调，实际使用时你可以换成 Int (R.drawable.xxx) 或 String (Url)
    val imagePlaceholderColor: Color,
    val resId: Int,
    val buttonText: String = "开始使用"
)

// 2. 主屏幕 Composable
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    pages: List<OnboardingPageData>,
    onSkipClicked: () -> Unit,
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // 对应白色卡片背景
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // --- 顶部 Skip 按钮 ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            TextButton(onClick = onSkipClicked) {
                Text(
                    text = "Skip",
                    color = Color.Gray,
                    fontSize = 16.sp
                )
            }
        }

        // --- 中间滑动内容 ---
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) { pageIndex ->
            OnboardingPageContent(data = pages[pageIndex])
        }

        // --- 底部控制区域 (指示器 + 按钮) ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 指示器
            PageIndicator(
                pageCount = pages.size,
                currentPage = pagerState.currentPage
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 动态按钮区域
            Box(contentAlignment = Alignment.Center) {
                val isLastPage = pagerState.currentPage == pages.size - 1

                // 场景 A: 非最后一页，显示圆形箭头按钮
                androidx.compose.animation.AnimatedVisibility(
                    visible = !isLastPage,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4A6EF6)) // 蓝色主色
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next",
                            tint = Color.White
                        )
                    }
                }

                // 场景 B: 最后一页，显示长条按钮
                androidx.compose.animation.AnimatedVisibility(
                    visible = isLastPage,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Button(
                        onClick = onFinish,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4A6EF6)
                        )
                    ) {
                        Text(
                            text = pages.last().buttonText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

// 3. 单页内容组件
@Composable
fun OnboardingPageContent(data: OnboardingPageData) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // --- 图片区域 (包含模拟的阴影) ---
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(280.dp) // 给图片区域一个固定大小
        ) {
            // 模拟光晕 (Glow)
            // 如果你有带阴影的图片，就把这个 Box 去掉，直接加载 Image
//            Box(
//                modifier = Modifier
//                    .size(200.dp)
//                    .background(
//                        brush = Brush.radialGradient(
//                            colors = listOf(
//                                data.imagePlaceholderColor.copy(alpha = 0.3f),
//                                Color.Transparent
//                            )
//                        )
//                    )
//            )

            Image(
                painter = painterResource(id = data.resId), // 假设你在 data 类里加了 imageResId
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(300.dp) // 调整合适大小
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // --- 文本区域 ---
        Text(
            text = data.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1C24),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = data.description,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.height(100.dp))
//        Text(text = "skip")
    }
}

// 4. 指示器组件 (Dots)
@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(pageCount) { index ->
            val isSelected = currentPage == index

            // 动画宽度：选中变宽
            val width by animateDpAsState(
                targetValue = if (isSelected) 24.dp else 8.dp,
                label = "dot_width"
            )

            val color = if (isSelected) Color(0xFF4A6EF6) else Color(0xFFE0E0E0)

            Box(
                modifier = Modifier
                    .height(8.dp)
                    .width(width)
                    .clip(CircleShape)
                    .background(color)
            )
        }
    }
}

@Composable
fun PreviewOnboarding(
    onFinish: () -> Unit = {},
) {
    val samplePages = listOf(
        OnboardingPageData(
            title = "跟踪你的进度",
            resId = R.drawable.img,
            description = "通过强大的数据分析获取深度洞察，可视化你的工作效率，按时完成任务。",
            imagePlaceholderColor = Color(0xFF6C63FF) // 模拟紫色图表
        ),

        OnboardingPageData(
            title = "管理你的任务",
            resId = R.drawable.img,
            description = "使用任务管理工具，轻松管理你的任务，完成工作，完成任务。",
            imagePlaceholderColor = Color(0xFF6C63FF) // 模拟紫色图表
        ),

        OnboardingPageData(
            title = "记录你的行动",
            resId = R.drawable.img,
            description = "自动记录你的行为",
            imagePlaceholderColor = Color(0xFF6C63FF) // 模拟紫色图表
        ),

        OnboardingPageData(
            title = "准备好使用 TaskFlow 了吗？",
            resId = R.drawable.img,
            description = "加入成千上万提升效率的团队吧，让我们一起高效完成每一天的任务。",
            imagePlaceholderColor = Color(0xFF4A6EF6) // 模拟蓝色 Icon
        )

    )

    MaterialTheme {
        OnboardingScreen(
            pages = samplePages,
            onSkipClicked = onFinish,
            onFinish = onFinish
        )
    }
}
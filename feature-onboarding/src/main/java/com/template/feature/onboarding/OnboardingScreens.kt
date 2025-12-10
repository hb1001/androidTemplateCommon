package com.template.feature.onboarding

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.template.core.common.permission.requestTestPermission
import com.template.core.ui.vant.VanButton
import com.template.data.network.update.XUpdateManager

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = 0,pageCount = {4})
    val context = LocalActivity.current
    Scaffold(
        topBar = { /* 不需要标题，留空即可 */ },
        bottomBar = {}
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalPager( state = pagerState) { page ->
                when (page) {
                    0 -> Box(modifier = Modifier.fillMaxSize()){
                        Column() {
                            CoachMarkDemoScreen()
                        }
                    }
                    1 -> Box(modifier = Modifier.fillMaxSize()){
                        Column(modifier = Modifier.fillMaxSize().align(Alignment.Center)) {
                            VanButton(text = "申请权限", onClick = {
                                initPermissionDelegate()
                                requestTestPermission(context!!)
                            })
                        }
                    }
                    2 -> Box(modifier = Modifier.fillMaxSize()){
                        Text("环境222222222222")
                        VanButton(text = "下一步", onClick = {
//                            val isUpdateNeeded = XUpdateManager.isUpdateNeeded(context, "1.0.0")
//                            Toast.makeText(context, "需要更新: ${isUpdateNeeded}", Toast.LENGTH_SHORT).show()
//                            if (isUpdateNeeded) {
                                XUpdateManager.checkUpdate(context as android.app.Activity)
//                            }
                        })
                    }
                    3 -> Box(modifier = Modifier.fillMaxSize()){
                        Text("环境0")
                        VanButton(text = "开始体验", onClick = onFinish)
                    }
                }
            }

            if (pagerState.currentPage == 3) {
                // Core-UI 提供的统一按钮
                VanButton(text = "开始体验", onClick = onFinish)
            }
        }

    }


}
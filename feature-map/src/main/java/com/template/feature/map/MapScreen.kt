package com.template.feature.map

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.template.core.ui.vant.VanImage
import com.template.core.ui.vant.VanImageFit

@Composable
fun MapScreen(
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MapContent()
}

@Composable
fun MapContent() {

    Box(
        modifier = Modifier.fillMaxSize().background(
            androidx.compose.ui.graphics.Color.Blue.copy(alpha = 0.2f)
        ),
        contentAlignment = Alignment.Center
    ) {
        VanImage(
            src = R.drawable.map_mock,
           fit = VanImageFit.Cover
        )

        Text("地图 map 页面 开发中")
    }
}

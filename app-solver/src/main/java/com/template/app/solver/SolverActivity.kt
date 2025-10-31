package com.template.app.solver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.template.core.ui.theme.AppTheme
import com.template.feature.solver.SolverScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SolverActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                SolverScreen()
            }
        }
    }
}
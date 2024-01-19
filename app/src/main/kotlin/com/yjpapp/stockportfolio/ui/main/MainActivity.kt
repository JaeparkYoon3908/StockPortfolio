package com.yjpapp.stockportfolio.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main
 * 디자인 패턴 : MVP
 * @author Yoon Jae-park
 * @since 2020.12
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(viewModel = viewModel)
            MainDialogWidget(viewModel = viewModel)
        }

    }
}
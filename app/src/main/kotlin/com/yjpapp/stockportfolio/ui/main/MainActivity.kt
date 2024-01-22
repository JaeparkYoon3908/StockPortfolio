package com.yjpapp.stockportfolio.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
            val snackState = SnackbarHostState()
            val toastMessageState by viewModel.toastMessageState.collectAsStateWithLifecycle(null)
            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                SnackbarHost(hostState = snackState, modifier = Modifier)
            }
            toastMessageState?.let { toastMessage ->
                LaunchedEffect(toastMessage) {
                    val message = toastMessage.message.ifEmpty {
                        getString(toastMessage.strResId, toastMessage.formatArgs)
                    }
                    snackState.showSnackbar(
                        message = message,
                        duration = toastMessage.duration,
                    )
                    viewModel.toastMessageShown()
                }
            }
        }
    }
}
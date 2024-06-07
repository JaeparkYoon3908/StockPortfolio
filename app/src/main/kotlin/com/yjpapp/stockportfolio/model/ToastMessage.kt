package com.yjpapp.stockportfolio.model

import androidx.compose.material3.SnackbarDuration

data class ToastMessage(
    val message: String = "",
    val strResId: Int = 0,
    val formatArgs: Any? = null,
    val duration: SnackbarDuration = SnackbarDuration.Short
)

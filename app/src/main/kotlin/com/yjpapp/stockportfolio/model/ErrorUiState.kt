package com.yjpapp.stockportfolio.model

data class ErrorUiState(
    val isError: Boolean = false,
    val errorCode: String = "",
    val errorMessage: String = ""
)

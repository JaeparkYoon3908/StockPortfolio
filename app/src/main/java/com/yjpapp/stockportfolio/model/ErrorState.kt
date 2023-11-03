package com.yjpapp.stockportfolio.model

data class ErrorState(
    val isError: Boolean = false,
    val errorCode: String = "",
    val errorMessage: String = ""
)

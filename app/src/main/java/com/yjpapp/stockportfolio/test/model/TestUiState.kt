package com.yjpapp.stockportfolio.test.model

// Models the message to show on the screen.
data class UserMessage(val id: Long, val message: String)

// Models the UI state for the Latest news screen.
data class LatestNewsUiState(
    val news: List<News> = emptyList(),
    val isLoading: Boolean = false,
    val userMessages: List<UserMessage> = emptyList()
)

data class News(
    val news: String
)
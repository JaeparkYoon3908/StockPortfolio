package com.yjpapp.stockportfolio.model

enum class Country(val type: Int, val title: String) {
    Korea(type = 1, title = "한국 주식"),
    Usa(type = 2, title = "미국 주식"),
}
package com.yjpapp.data.model.request

data class ReqUsaStockPriceInfo(
    val function: String = "SYMBOL_SEARCH",
    val keywords: String = "",
    val apikey: String = "",
)

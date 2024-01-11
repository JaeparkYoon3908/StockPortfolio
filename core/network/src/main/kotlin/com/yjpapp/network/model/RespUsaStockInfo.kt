package com.yjpapp.network.model

import kotlinx.serialization.SerialName

data class RespUsaStockInfo(
    @SerialName("Time Series (1min)")
    val infoList: List<Data> = listOf()
) {
    data class Data(
        @SerialName("1. open")
        val open: String = "",
        @SerialName("2. high")
        val high: String = "",
        @SerialName("3. low")
        val low: String = "",
        @SerialName("4. close")
        val close: String = "",
        @SerialName("5. volume")
        val volume: String = "",
    )
}
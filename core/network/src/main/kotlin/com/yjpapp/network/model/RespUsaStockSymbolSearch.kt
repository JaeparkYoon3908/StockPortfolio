package com.yjpapp.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RespUsaStockSymbolSearch(
    val bestMatches: List<SymbolData> = listOf()
)
@Serializable
data class SymbolData(
    @SerialName("1. symbol")
    val symbol: String = "",
    @SerialName("2. name")
    val name: String = "",
    @SerialName("3. type")
    val type: String = "",
    @SerialName("4. region")
    val region: String = "",
    @SerialName("5. marketOpen")
    val marketOpen: String = "",
    @SerialName("6. marketClose")
    val marketClose: String = "",
    @SerialName("7. timezone")
    val timezone: String = "",
    @SerialName("8. currency")
    val currency: String = "",
    @SerialName("9. matchScore")
    val matchScore: String = ""
)
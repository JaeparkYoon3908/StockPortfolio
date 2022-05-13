package com.yjpapp.stockportfolio.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespTotalGainIncomeNoteData(
    var total_price: Double = 0.0,
    var total_percent: Double = 0.00
)
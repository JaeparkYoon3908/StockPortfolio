package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName

data class RespTotalGainIncomeNoteData(
    @SerializedName("total_price")
    var total_price: Double = 0.0,
    @SerializedName("total_percent")
    var total_percent: Double = 0.00
)
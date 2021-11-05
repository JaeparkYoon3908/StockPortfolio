package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName

data class RespIncomeNoteInfo (
    @SerializedName("id")
    var id: Int = 0,
    @SerializedName("subjectName")
    var subjectName: String = "", // 종목명
    @SerializedName("realPainLossesAmount")
    var realPainLossesAmount: Double = 0.00, //순손익금액
    @SerializedName("sellDate")
    var sellDate: String = "", //매도일
    @SerializedName("gainPercent")
    var gainPercent: Double = 0.00, // 수익률
    @SerializedName("purchasePrice")
    var purchasePrice: Double = 0.00, // 매수단가
    @SerializedName("sellPrice")
    var sellPrice: Double = 0.00, // 매도단가
    @SerializedName("sellCount")
    var sellCount: Int = 0,
)

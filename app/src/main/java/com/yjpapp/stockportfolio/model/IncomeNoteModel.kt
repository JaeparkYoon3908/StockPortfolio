package com.yjpapp.stockportfolio.model

import com.google.gson.annotations.SerializedName

data class IncomeNoteModel(
    @SerializedName("id")
    var id: Int,
    @SerializedName("subjectName")
    var subjectName: String?, // 종목명
    @SerializedName("realPainLossesAmount")
    var realPainLossesAmount: String?, //순손익금액
    @SerializedName("sellDate")
    var sellDate: String?, //매도일
    @SerializedName("gainPercent")
    var gainPercent: String?, // 수익률
    @SerializedName("purchasePrice")
    var purchasePrice: String?, // 매수단가
    @SerializedName("sellPrice")
    var sellPrice: String?, // 매도단가
    @SerializedName("sellCount")
    var sellCount: Int // 매도수량
)
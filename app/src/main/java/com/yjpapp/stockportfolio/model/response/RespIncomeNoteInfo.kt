package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName


data class RespIncomeNoteInfo(
    @SerializedName("page_info")
    var page_info: PageInfo,
    @SerializedName("income_note")
    var income_note: ArrayList<IncomeNoteList>
){
    data class PageInfo(
        @SerializedName("page")
        var page: String?,
        @SerializedName("page_size")
        var page_size: String?,
        @SerializedName("total_elements")
        var total_elements: Int
        )

    data class IncomeNoteList(
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
}
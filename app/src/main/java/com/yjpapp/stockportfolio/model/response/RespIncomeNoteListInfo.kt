package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName


data class RespIncomeNoteListInfo(
    @SerializedName("page_info")
    var page_info: PageInfo = PageInfo(),
    @SerializedName("total_profit_or_loss_info")
    var total_profit_or_loss_info: TotalProfitOrLossInfo = TotalProfitOrLossInfo(),
    @SerializedName("income_note")
    var income_note: ArrayList<IncomeNoteInfo> = arrayListOf()
){
    data class PageInfo(
        @SerializedName("page")
        var page: String = "",
        @SerializedName("page_size")
        var page_size: String = "",
        @SerializedName("total_elements")
        var total_elements: Int = 0
        )

    data class TotalProfitOrLossInfo(
        @SerializedName("total_price")
        var totalPrice: Double = 0.0,
        @SerializedName("total_percent")
        var totalPercent: String = "")

    data class IncomeNoteInfo(
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
        var sellCount: Int = 0) {
    }
}
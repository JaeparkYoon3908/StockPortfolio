package com.yjpapp.stockportfolio.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespIncomeNoteListInfo(
    var page_info: PageInfo = PageInfo(),
    var total_profit_or_loss_info: TotalProfitOrLossInfo = TotalProfitOrLossInfo(),
    var income_note: ArrayList<IncomeNoteInfo> = arrayListOf()
){
    @Serializable
    data class PageInfo(
        var page: String = "",
        var page_size: String = "",
        var total_elements: Int = 0
        )

    @Serializable
    data class TotalProfitOrLossInfo(
        var totalPrice: Double = 0.0,
        var totalPercent: String = "")

    @Serializable
    data class IncomeNoteInfo(
        var id: Int = 0,
        var subjectName: String = "", // 종목명
        var realPainLossesAmount: Double = 0.00, //순손익금액
        var sellDate: String = "", //매도일
        var gainPercent: Double = 0.00, // 수익률
        var purchasePrice: Double = 0.00, // 매수단가
        var sellPrice: Double = 0.00, // 매도단가
        var sellCount: Int = 0) {
    }
}
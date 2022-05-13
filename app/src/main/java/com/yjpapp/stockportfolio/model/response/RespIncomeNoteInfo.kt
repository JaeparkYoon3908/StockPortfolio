package com.yjpapp.stockportfolio.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespIncomeNoteInfo (
    var id: Int = 0,
    var subjectName: String = "", // 종목명
    var realPainLossesAmount: Double = 0.00, //순손익금액
    var sellDate: String = "", //매도일
    var gainPercent: Double = 0.00, // 수익률
    var purchasePrice: Double = 0.00, // 매수단가
    var sellPrice: Double = 0.00, // 매도단가
    var sellCount: Int = 0,
)

package com.yjpapp.stockportfolio.model

data class DataInfo (
    var id: Int,
    var subjectName: String?, // 종목명
    var realPainLossesAmount: String?, //순손익금액
    var purchaseDate: String?, // 매수일
    var sellDate: String?, //매도일
    var gainPercent: String?, // 수익률
    var purchasePrice: String?, // 매수단가
    var sellPrice: String?, // 매도단가
    var sellCount: Int // 매도수량
//    var isDeleteCheck: String?
)
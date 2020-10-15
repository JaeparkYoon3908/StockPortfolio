package com.yjpapp.stockportfolio.model

data class DataInfo (
    var dateOfSale: String?, //매매일
    var subjectName: String?, // 종목명
    var realPainLossesAmount: String?, //순손익금액
    var gainPercent: String?, // 수익률
    var purchasePrice: String?, // 매수단가
    var sellPrice: String?, // 매도단가
    var isDeleteCheck: Boolean
)






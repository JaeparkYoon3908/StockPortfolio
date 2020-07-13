package com.yjpapp.stockportfolio.database

class DataInfo {
    private var id: Int = 0
    private lateinit var date_of_sale: String //매매일
    private lateinit var subject_name: String //종목명
    private lateinit var real_gain_losses_amount: String //순손익금액
    private lateinit var gain_percent: String // 수익률

}
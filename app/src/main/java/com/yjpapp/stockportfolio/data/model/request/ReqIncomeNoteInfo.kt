package com.yjpapp.stockportfolio.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ReqIncomeNoteInfo(
    var id: Int = -1,
    var subjectName: String = "",
    var sellDate: String = "",
    var purchasePrice: Double = 0.00,
    var sellPrice: Double = 0.00,
    var sellCount: Int = 0
)

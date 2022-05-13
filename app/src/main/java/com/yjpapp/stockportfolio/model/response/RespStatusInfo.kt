package com.yjpapp.stockportfolio.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespStatusInfo(
    var status: Int,
    var msg: String?
)
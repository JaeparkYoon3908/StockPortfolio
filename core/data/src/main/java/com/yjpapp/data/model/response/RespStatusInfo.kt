package com.yjpapp.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespStatusInfo(
    var status: Int,
    var msg: String?
)
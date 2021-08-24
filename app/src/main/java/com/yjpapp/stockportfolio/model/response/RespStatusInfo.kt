package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName

data class RespStatusInfo(
    @SerializedName("status")
    var status: Int,
    @SerializedName("msg")
    var msg: String?
)
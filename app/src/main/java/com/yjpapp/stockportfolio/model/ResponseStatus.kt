package com.yjpapp.stockportfolio.model

import com.google.gson.annotations.SerializedName

data class ResponseStatus(
    @SerializedName("status")
    var status: Int,
    @SerializedName("msg")
    var msg: String?
    )
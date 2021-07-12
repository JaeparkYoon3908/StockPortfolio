package com.yjpapp.stockportfolio.model

import com.google.gson.annotations.SerializedName

data class SNSLoginResult(
    @SerializedName("userIndex")
    var userIndex: Int,
    @SerializedName("email")
    var email: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("token")
    var token: String?
    )

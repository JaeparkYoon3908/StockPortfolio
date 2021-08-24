package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName

data class RespLoginUserInfo(
    @SerializedName("userIndex")
    var userIndex: Int,
    @SerializedName("email")
    var email: String?,
    @SerializedName("name")
    var name: String?,
    @SerializedName("login_type")
    var login_type: String?,
    @SerializedName("token")
    var token: String?
    )

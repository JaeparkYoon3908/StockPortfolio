package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName

data class RespNaverDeleteUserInfo (
    @SerializedName("access_token")
    var access_token: String = "",
    @SerializedName("result")
    var result: String = "",
    @SerializedName("error")
    var error: String = "",
    @SerializedName("error_description")
    var error_description: String = ""
)
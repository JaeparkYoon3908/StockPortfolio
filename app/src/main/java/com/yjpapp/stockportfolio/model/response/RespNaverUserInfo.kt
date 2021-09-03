package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName

data class RespNaverUserInfo (
    @SerializedName("resultcode")
    var resultcode: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("response")
    var response: UserInfo = UserInfo()
){
    data class UserInfo(
        @SerializedName("id")
        var id: String = "",
        @SerializedName("gender")
        var gender: String = "M",
        @SerializedName("email")
        var email: String = "",
        @SerializedName("name")
        var name: String = ""
    )
}
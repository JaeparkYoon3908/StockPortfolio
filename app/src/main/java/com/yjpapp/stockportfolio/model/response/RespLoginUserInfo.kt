package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName

data class RespLoginUserInfo(
    @SerializedName("status")
    var status: Int = 0,
    @SerializedName("data")
    var data: UserInfo = UserInfo()
){
    data class UserInfo (
        @SerializedName("user_index")
        var userIndex: Int = 0,
        @SerializedName("user_email")
        var email: String = "",
        @SerializedName("user_name")
        var name: String = "",
        @SerializedName("login_type")
        var login_type: String = "",
        @SerializedName("token")
        var token: String = ""
    )
}

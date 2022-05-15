package com.yjpapp.stockportfolio.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespLoginUserInfo(
    var status: Int = 0,
    var data: UserInfo = UserInfo()
){
    @Serializable
    data class UserInfo (
        var user_index: Int = 0,
        var user_email: String = "",
        var user_name: String = "",
        var login_type: String = "",
        var token: String = ""
    )
}

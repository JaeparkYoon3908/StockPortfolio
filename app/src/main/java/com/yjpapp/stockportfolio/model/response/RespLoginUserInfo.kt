package com.yjpapp.stockportfolio.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespLoginUserInfo(
    var status: Int = 0,
    var data: UserInfo = UserInfo()
){
    @Serializable
    data class UserInfo (
        var userIndex: Int = 0,
        var email: String = "",
        var name: String = "",
        var login_type: String = "",
        var token: String = ""
    )
}

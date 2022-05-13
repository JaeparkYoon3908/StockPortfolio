package com.yjpapp.stockportfolio.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespGetNaverUserInfo (
    var resultcode: String = "",
    var message: String = "",
    var response: UserInfo = UserInfo()
){
    @Serializable
    data class UserInfo(
        var id: String = "",
        var gender: String = "M",
        var email: String = "",
        var name: String = ""
    )
}
package com.yjpapp.stockportfolio.data.model.response

import kotlinx.serialization.Serializable

@Serializable
data class RespNaverDeleteUserInfo (
    var access_token: String = "",
    var result: String = "",
    var error: String = "",
    var error_description: String = ""
)
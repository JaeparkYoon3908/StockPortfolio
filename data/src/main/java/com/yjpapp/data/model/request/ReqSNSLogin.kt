package com.yjpapp.data.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ReqSNSLogin(
    var user_email: String = "",
    var user_name: String = "",
    var login_type: String = ""
    )

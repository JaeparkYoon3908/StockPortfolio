package com.yjpapp.stockportfolio.model.request

data class ReqSNSLogin(
    var user_email: String = "",
    var user_name: String = "",
    var login_type: String = ""
    )

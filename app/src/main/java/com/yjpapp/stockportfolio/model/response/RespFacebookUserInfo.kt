package com.yjpapp.stockportfolio.model.response

import com.google.gson.annotations.SerializedName

data class RespFacebookUserInfo (
    @SerializedName("name")
    var name: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("id")
    var id: String = ""
)
package com.yjpapp.stockportfolio.model

import com.google.gson.annotations.SerializedName

data class TestModel(
    @SerializedName("company")
    var company: String,
    @SerializedName("price")
    var name: String,
    @SerializedName("date")
    var date: String,
    @SerializedName("msg")
    var msg: String
    )

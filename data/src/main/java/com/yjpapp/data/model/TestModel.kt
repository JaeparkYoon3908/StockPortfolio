package com.yjpapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class TestModel(
    var company: String,
    var name: String,
    var date: String,
    var msg: String
    )

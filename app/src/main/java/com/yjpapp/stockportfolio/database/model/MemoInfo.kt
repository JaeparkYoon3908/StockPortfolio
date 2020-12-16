package com.yjpapp.stockportfolio.database.model

data class MemoInfo(
    var id: Int,
    var date: String?,
    var title: String?,
    var content: String?,
    var deleteChecked: Boolean
    )


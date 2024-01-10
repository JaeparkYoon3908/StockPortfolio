package com.yjpapp.data.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UsaStockSymbolData(
    val symbol: String = "",
    val name: String = "",
    val type: String = "",
) : Parcelable
package com.yjpapp.stockportfolio.util

import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getTodayYYYYMMDD(): String {
        val currentTime: Long = System.currentTimeMillis()
        val todayDate = Date(currentTime)
        val sdformat = SimpleDateFormat("yyyyMMdd")
        val today: String = sdformat.format(todayDate)

        return today
    }
}
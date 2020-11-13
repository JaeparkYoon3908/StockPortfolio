package com.yjpapp.stockportfolio.util

import android.util.Log
import com.yjpapp.stockportfolio.BuildConfig
import java.text.SimpleDateFormat
import java.util.*

object Utils {
    fun getTodayYYYYMMDD(): String {
        val currentTime: Long = System.currentTimeMillis()
        val todayDate = Date(currentTime)
        val sdformat = SimpleDateFormat("yyyyMMdd")
        return sdformat.format(todayDate)
    }

    fun logcat(msg: String){
        if(BuildConfig.LOG_CAT) Log.d(javaClass.simpleName, msg)
    }
}
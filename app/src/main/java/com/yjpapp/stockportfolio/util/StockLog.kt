package com.yjpapp.stockportfolio.util

import android.util.Log
import com.yjpapp.stockportfolio.BuildConfig

object StockLog {
    fun d(tag: String?, msg: String) {
//        if (BuildConfig.LOG_CAT) Log.d(tag, msg)
    }

    fun e(tag: String?, msg: String) {
//        if (BuildConfig.LOG_CAT) Log.e(tag, msg)
    }

    fun i(tag: String?, msg: String) {
//        if (BuildConfig.LOG_CAT) Log.i(tag, msg)
    }

    fun v(tag: String?, msg: String) {
//        if (BuildConfig.LOG_CAT) Log.v(tag, msg)
    }
}
package com.yjpapp.data

import android.util.Log

object StockLog {
    fun d(tag: String?, msg: String) {
        if (BuildConfig.LOG_CAT) Log.d(tag, msg)
    }

    fun d(tag: String?, msg: String, tr: Throwable) {
        if (BuildConfig.LOG_CAT) Log.d(tag, msg, tr)
    }

    fun e(tag: String?, msg: String) {
        if (BuildConfig.LOG_CAT) Log.e(tag, msg)
    }

    fun e(tag: String?, msg: String, tr: Throwable) {
        if (BuildConfig.LOG_CAT) Log.e(tag, msg, tr)
    }

    fun i(tag: String?, msg: String) {
        if (BuildConfig.LOG_CAT) Log.i(tag, msg)
    }

    fun i(tag: String?, msg: String, tr: Throwable) {
        if (BuildConfig.LOG_CAT) Log.i(tag, msg, tr)
    }

    fun v(tag: String?, msg: String) {
        if (BuildConfig.LOG_CAT) Log.v(tag, msg)
    }

    fun v(tag: String?, msg: String, tr: Throwable) {
        if (BuildConfig.LOG_CAT) Log.v(tag, msg, tr)
    }

    fun wtf(tag: String?, msg: String) {
        if (BuildConfig.LOG_CAT) Log.wtf(tag, msg)
    }
    
    fun wtf(tag: String?, tr: Throwable) {
        if (BuildConfig.LOG_CAT) Log.wtf(tag, tr)
    }

    fun wtf(tag: String?, msg: String, tr: Throwable) {
        if (BuildConfig.LOG_CAT) Log.wtf(tag, msg, tr)
    }
}
package com.yjpapp.stockportfolio.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

/**
 *
 */
open class BaseActivity(resId: Int): AppCompatActivity(resId) {
    lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
    }

}
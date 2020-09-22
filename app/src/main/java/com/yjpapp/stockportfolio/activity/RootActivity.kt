package com.yjpapp.stockportfolio.activity

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.BuildConfig
import java.util.logging.Logger

open class RootActivity : AppCompatActivity {

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }
    constructor(contentLayout: Int) : super(contentLayout)

    fun logcat(msg: String){
        if(BuildConfig.LOG_CAT) Log.d(javaClass.simpleName, msg)
    }
}
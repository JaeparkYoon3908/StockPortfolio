package com.yjpapp.stockportfolio.activity

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.BuildConfig
import java.util.logging.Logger

open class RootActivity : AppCompatActivity {
    constructor(contentLayout: Int) : super(contentLayout)

    fun logcat(msg: String){
        if(BuildConfig.LOG_CAT) Log.d(javaClass.simpleName, msg);
    }
}
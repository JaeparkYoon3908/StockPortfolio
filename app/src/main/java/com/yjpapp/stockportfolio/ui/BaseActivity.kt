package com.yjpapp.stockportfolio.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.preference.PreferenceController

open class BaseActivity(contentLayout: Int) : AppCompatActivity(contentLayout) {
    lateinit var mContext: Context
    lateinit var databaseController: DatabaseController
    lateinit var preferenceController: PreferenceController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        databaseController = DatabaseController.getInstance(mContext)
        preferenceController = PreferenceController.getInstance(mContext)
    }

    fun logcat(msg: String){
        if(BuildConfig.LOG_CAT) Log.d(javaClass.simpleName, msg)
    }
}
package com.yjpapp.stockportfolio.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.database.preference.PreferenceController
import com.yjpapp.stockportfolio.database.sqlte.DatabaseController

abstract class BaseActivity : AppCompatActivity() {
    val mContext by lazy { this }
    val databaseController by lazy { DatabaseController.getInstance(mContext) }
    val preferenceController by lazy { PreferenceController.getInstance(mContext) }
}
package com.yjpapp.stockportfolio.base

import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {
    val mContext by lazy { this }
    val preferenceController: PreferenceController by inject()
}
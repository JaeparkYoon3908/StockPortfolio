package com.yjpapp.stockportfolio.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.database.DatabaseController

open class BaseActivity(contentLayout: Int) : AppCompatActivity(contentLayout) {
    lateinit var mContext: Context
    lateinit var databaseController: DatabaseController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = this
        databaseController = DatabaseController.getInstance(mContext)

    }
}
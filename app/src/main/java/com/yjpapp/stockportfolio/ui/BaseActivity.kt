package com.yjpapp.stockportfolio.ui

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.database.DatabaseOpenHelper

open class BaseActivity(contentLayout: Int) : AppCompatActivity(contentLayout) {
    public var mContext: Context? = this
    public var databaseController:DatabaseController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_root)

        val dbHelper = DatabaseOpenHelper(this)
        val sqliteDatabases = dbHelper.writableDatabase
        databaseController = DatabaseController(sqliteDatabases, dbHelper)
    }
}
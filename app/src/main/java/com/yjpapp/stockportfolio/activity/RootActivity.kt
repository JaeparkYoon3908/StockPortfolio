package com.yjpapp.stockportfolio.activity

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.DatabaseHandler
import com.yjpapp.stockportfolio.database.DatabaseOpenHelper
import java.util.logging.Logger

open class RootActivity(contentLayout: Int) : AppCompatActivity(contentLayout) {
    public var mContext: Context? = this
    public var databaseHandler:DatabaseHandler? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_root)

        val dbHelper = DatabaseOpenHelper(this)
        val sqliteDatabases = dbHelper.writableDatabase
        databaseHandler = DatabaseHandler(sqliteDatabases, dbHelper)
    }
}
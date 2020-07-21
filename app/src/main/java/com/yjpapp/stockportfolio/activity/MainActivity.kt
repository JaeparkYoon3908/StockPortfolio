package com.yjpapp.stockportfolio.activity

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.DataInfo
import com.yjpapp.stockportfolio.database.DatabaseHandler
import com.yjpapp.stockportfolio.database.DatabaseOpenHelper
import com.yjpapp.stockportfolio.database.Databases

class MainActivity : RootActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* db 생성코드
        val dbHelper = DatabaseOpenHelper(this)
        val sqliteDatabases = dbHelper.writableDatabase
        val databaseHandler = DatabaseHandler(sqliteDatabases, dbHelper)

        val dataInfo = DataInfo()
        dataInfo.gainPercent = "10%"
        dataInfo.realPainLossesAmount = "test"
        databaseHandler.insertData(dataInfo)

         */
    }
}
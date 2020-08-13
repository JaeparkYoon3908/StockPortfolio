package com.yjpapp.stockportfolio.activity

import android.os.Bundle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.DataInfo
import com.yjpapp.stockportfolio.database.DatabaseHandler
import com.yjpapp.stockportfolio.database.DatabaseOpenHelper

class MainActivity : RootActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    // DB 생성 코드
    private fun createDB(){
        val dbHelper = DatabaseOpenHelper(this)
        val sqliteDatabases = dbHelper.writableDatabase
        val databaseHandler = DatabaseHandler(sqliteDatabases, dbHelper)

        val dataInfo = DataInfo()
        dataInfo.gainPercent = "10%"
        dataInfo.realPainLossesAmount = "test"
        databaseHandler.insertData(dataInfo)
    }
}
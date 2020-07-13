package com.yjpapp.stockportfolio.database

import android.database.sqlite.SQLiteDatabase

class DataBaseHandler(private var database: SQLiteDatabase, private val dbHelper: DatabaseOpenHelper) {
    public fun open(){
        database = dbHelper.writableDatabase
    }
    public fun close(){
        dbHelper.close()
    }

    public fun insertData():Boolean{
        return true
    }
}
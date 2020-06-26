package com.yjpapp.stockportfolio.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(context: Context): SQLiteOpenHelper(context,"database",null,1) {

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(Databases.CreateDB.COL_ID)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}
package com.yjpapp.stockportfolio.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(context: Context): SQLiteOpenHelper(context, "database.db", null, 1) {
    private val DB_VERSION = 1

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createMemoTable())
        db?.execSQL(createPortfolioTable())
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        db?.execSQL(createMemoTable())
//        db?.execSQL(createPortfolioTable())
    }

//    override fun onConfigure(db: SQLiteDatabase) {
//        super.onConfigure(db)
//        db.disableWriteAheadLogging()
//    }

    private fun createMemoTable(): String{
        return "create table " + Databases.TABLE_MEMO + "(" +
                Databases.COL_MEMO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Databases.COL_MEMO_DATE + " TEXT," +
                Databases.COL_MEMO_TITLE + " TEXT," +
                Databases.COL_MEMO_CONTENT + " TEXT);"
    }

    private fun createPortfolioTable(): String{
        return "create table " + Databases.TABLE_PORTFOLIO + "(" +
                Databases.COL_PORTFOLIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Databases.COL_PORTFOLIO_SUBJECT_NAME + " TEXT," +
                Databases.COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT + " TEXT," +
                Databases.COL_PORTFOLIO_PURCHASE_DATE + " TEXT," +
                Databases.COL_PORTFOLIO_SELL_DATE + " TEXT," +
                Databases.COL_PORTFOLIO_GAIN_PERCENT + " TEXT," +
                Databases.COL_PORTFOLIO_PURCHASE_PRICE + " TEXT," +
                Databases.COL_PORTFOLIO_SELL_PRICE + " TEXT," +
                Databases.COL_PORTFOLIO_SELL_COUNT + " TEXT);"
    }
}
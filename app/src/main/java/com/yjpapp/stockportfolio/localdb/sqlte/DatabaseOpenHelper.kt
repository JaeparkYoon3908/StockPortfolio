package com.yjpapp.stockportfolio.localdb.sqlte

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(context: Context): SQLiteOpenHelper(context, "database.db", null, 1) {
    private val DB_VERSION = 1

    override fun onCreate(db: SQLiteDatabase?) {
        db?.version = DB_VERSION
        db?.execSQL(createMemoTable())
        db?.execSQL(createIncomeNoteTable())
        db?.execSQL(createMyStockTable())
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
                Databases.COL_MEMO_CONTENT + " TEXT," +
                Databases.COL_MEMO_DELETE_CHECK + " TEXT DEFAULT false);"
    }

    private fun createIncomeNoteTable(): String{
        return "create table " + Databases.TABLE_INCOME_NOTE + "(" +
                Databases.COL_INCOME_NOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Databases.COL_INCOME_NOTE_SUBJECT_NAME + " TEXT," +
                Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT + " TEXT," +
//                Databases.COL_INCOME_NOTE_PURCHASE_DATE + " TEXT," +
                Databases.COL_INCOME_NOTE_SELL_DATE + " TEXT," +
                Databases.COL_INCOME_NOTE_GAIN_PERCENT + " TEXT," +
                Databases.COL_INCOME_NOTE_PURCHASE_PRICE + " TEXT," +
                Databases.COL_INCOME_NOTE_SELL_PRICE + " TEXT," +
                Databases.COL_INCOME_NOTE_SELL_COUNT + " TEXT);"
    }

    private fun createMyStockTable(): String{
        return "create table " + Databases.TABLE_MY_STOCK + "(" +
                Databases.COL_MY_STOCK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                Databases.COL_MY_STOCK_SUBJECT_NAME + " TEXT," +
                Databases.COL_MY_STOCK_REAL_GAINS_LOSSES_AMOUNT + " TEXT," +
                Databases.COL_MY_STOCK_GAIN_PERCENT + " TEXT," +
                Databases.COL_MY_STOCK_PURCHASE_DATE + " TEXT," +
                Databases.COL_MY_STOCK_PURCHASE_PRICE + " TEXT," +
                Databases.COL_MY_STOCK_CURRENT_PRICE + " TEXT," +
                Databases.COL_MY_STOCK_PURCHASE_COUNT + " TEXT);"
    }
}
package com.yjpapp.stockportfolio.base

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.yjpapp.stockportfolio.localdb.sqlte.DatabaseOpenHelper

open class BaseInteractor {
    companion object {
        @Volatile private var instance: BaseInteractor? = null
        //        private lateinit var databaseController: DatabaseController
        lateinit var dbHelper: DatabaseOpenHelper
        lateinit var database: SQLiteDatabase
        @JvmStatic
        fun getInstance(context: Context): BaseInteractor =
                instance ?: synchronized(this) {
                    instance ?: BaseInteractor().also {
                        instance = it
//                        databaseController = DatabaseController.getInstance(mContext)
                        dbHelper = DatabaseOpenHelper(context)
                        database = dbHelper.writableDatabase
                    }
                }

    }
}
package com.yjpapp.stockportfolio.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.model.HeadInfo

class DatabaseHandler(var database: SQLiteDatabase, var dbHelper: DatabaseOpenHelper) {

    fun insertData(dataInfo: DataInfo): Boolean{
        var insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_DATE_OF_SALE, dataInfo.dateOfSale)
        contentValues.put(Databases.COL_SUBJECT_NAME,dataInfo.subjectName)
        contentValues.put(Databases.COL_REAL_GAINS_LOSSES_AMOUNT, dataInfo.realPainLossesAmount)
        contentValues.put(Databases.COL_GAIN_PERCENT, dataInfo.gainPercent)
        contentValues.put(Databases.COL_PURCHASE_PRICE, dataInfo.purchasePrice)
        contentValues.put(Databases.COL_SELL_PRICE, dataInfo.sellPrice)

        insertCheck = database.insert(Databases.TABLE_DATA,null, contentValues)

        return insertCheck != -1L
    }

    fun insertHead(headInfo: HeadInfo): Boolean{
        var insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_PERIOD, headInfo.period)
        contentValues.put(Databases.COL_SUBJECT, headInfo.subject)

        insertCheck = database.insert(Databases.TABLE_HEAD, null, contentValues)

        return insertCheck != -1L
    }

    fun getAllDataInfo(): DataInfo {


        return DataInfo(null, null, null, null,
            null,null,false)
    }

    fun getDataInfo(position: Int): DataInfo {
        return DataInfo(null, null, null, null,
        null,null,false)
    }

    fun deleteDataInfo(position: Int){

    }
}
package com.yjpapp.stockportfolio.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.model.HeadInfo
import java.lang.StringBuilder

class DatabaseHandler(var database: SQLiteDatabase, var dbHelper: DatabaseOpenHelper) {
    //TODO DatabaseHandler를 getInst하여 DatabaseHandler.getInst(context).insert.... 할 수 있도록 작업하기.

    fun insertHead(headInfo: HeadInfo): Boolean{
        var insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_PERIOD, headInfo.period)
        contentValues.put(Databases.COL_SUBJECT, headInfo.subject)

        insertCheck = database.insert(Databases.TABLE_HEAD, null, contentValues)

        return insertCheck != -1L
    }

    fun insertData(dataInfo: DataInfo): Boolean{
        var insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_DATE_OF_SALE, dataInfo.dateOfSale)
        contentValues.put(Databases.COL_SUBJECT_NAME,dataInfo.subjectName)
        contentValues.put(Databases.COL_REAL_GAINS_LOSSES_AMOUNT, dataInfo.realPainLossesAmount)
        contentValues.put(Databases.COL_GAIN_PERCENT, dataInfo.gainPercent)
        contentValues.put(Databases.COL_PURCHASE_PRICE, dataInfo.purchasePrice)
        contentValues.put(Databases.COL_SELL_PRICE, dataInfo.sellPrice)
        contentValues.put(Databases.COL_DELETE_CHECK, dataInfo.isDeleteCheck)

        insertCheck = database.insert(Databases.TABLE_DATA,null, contentValues)

        return insertCheck != -1L
    }

    fun getAllDataInfo(): ArrayList<DataInfo>? {
        val cursor: Cursor
        val resultList = ArrayList<DataInfo>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_DATA)
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count>0){
            cursor.moveToFirst()
            while (cursor.moveToNext()){
                val result = DataInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_DATE_OF_SALE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_SELL_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_DELETE_CHECK))
                    )
                resultList.add(result)
            }
        }
        cursor?.close()
        return resultList
    }

    fun getDataInfo(position: Int): DataInfo? {
        val cursor: Cursor
        val result = DataInfo(0,null, null, null, null, null, null, null)
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_DATA + "WHERE ")
        sb.append("id = '$position'")
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count == 1){
            cursor.moveToFirst()
            result.id = cursor.getInt(0)
            result.dateOfSale = cursor.getString(1)
            result.subjectName = cursor.getString(2)
            result.realPainLossesAmount = cursor.getString(3)
            result.gainPercent = cursor.getString(4)
            result.purchasePrice = cursor.getString(5)
            result.sellPrice = cursor.getString(6)
            result.isDeleteCheck = cursor.getString(7)
        }
        cursor?.close()
        return result
    }



    fun deleteAllDataInfo(){
        //TODO ALL Delete 쿼리 코드 삽입.
    }
    fun deleteDataInfo(position: Int){
        //TODO Delete 쿼리 코드 삽입.
    }
}
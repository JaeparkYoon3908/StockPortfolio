package com.yjpapp.stockportfolio.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.yjpapp.stockportfolio.model.DataInfo
import com.yjpapp.stockportfolio.model.HeadInfo
import com.yjpapp.stockportfolio.util.Utils

class DatabaseController {
    // 싱글톤 패턴 적용 완료
    companion object {
        @Volatile private var instance: DatabaseController? = null
        private lateinit var mContext: Context
        private lateinit var dbHelper: DatabaseOpenHelper
        private lateinit var database:SQLiteDatabase

        @JvmStatic
        fun getInstance(context: Context): DatabaseController =
            instance ?: synchronized(this) {
                instance ?: DatabaseController().also {
                    instance = it
                    mContext = context
                    dbHelper = DatabaseOpenHelper(mContext)
                    database = dbHelper.writableDatabase
                }
            }
    }
    fun insertHead(headInfo: HeadInfo): Boolean{
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_PERIOD, headInfo.period)
        contentValues.put(Databases.COL_SUBJECT, headInfo.subject)
        insertCheck = database.insert(Databases.TABLE_HEAD, null, contentValues)

        return insertCheck != -1L
    }

    fun insertData(dataInfo: DataInfo?): Boolean{
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_SUBJECT_NAME,dataInfo?.subjectName)
        contentValues.put(Databases.COL_REAL_GAINS_LOSSES_AMOUNT, dataInfo?.realPainLossesAmount)
        contentValues.put(Databases.COL_PURCHASE_DATE, dataInfo?.purchaseDate)
        contentValues.put(Databases.COL_SELL_DATE, dataInfo?.sellDate)
        contentValues.put(Databases.COL_GAIN_PERCENT, dataInfo?.gainPercent)
        contentValues.put(Databases.COL_PURCHASE_PRICE, dataInfo?.purchasePrice)
        contentValues.put(Databases.COL_SELL_PRICE, dataInfo?.sellPrice)
        contentValues.put(Databases.COL_SELL_COUNT, dataInfo?.sellCount)

        insertCheck = database.insert(Databases.TABLE_DATA,null, contentValues)

        return insertCheck != -1L
    }

    fun updateData(dataInfo: DataInfo?): Boolean{
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_SUBJECT_NAME,dataInfo?.subjectName)
        contentValues.put(Databases.COL_REAL_GAINS_LOSSES_AMOUNT, dataInfo?.realPainLossesAmount)
        contentValues.put(Databases.COL_PURCHASE_DATE, dataInfo?.purchaseDate)
        contentValues.put(Databases.COL_SELL_DATE, dataInfo?.sellDate)
        contentValues.put(Databases.COL_GAIN_PERCENT, dataInfo?.gainPercent)
        contentValues.put(Databases.COL_PURCHASE_PRICE, dataInfo?.purchasePrice)
        contentValues.put(Databases.COL_SELL_PRICE, dataInfo?.sellPrice)
        contentValues.put(Databases.COL_SELL_COUNT, dataInfo?.sellCount)

        updateCheck = database.update(Databases.TABLE_DATA, contentValues,
            Databases.COL_ID + " = ? ", arrayOf(dataInfo?.id.toString()))

        return updateCheck != -1
    }

    fun getAllDataInfo(): ArrayList<DataInfo?>? {
        val cursor: Cursor
        val resultList = ArrayList<DataInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_DATA)
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count>0){
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                val result = DataInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_ID)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_SUBJECT_NAME)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_REAL_GAINS_LOSSES_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PURCHASE_DATE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_SELL_DATE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_GAIN_PERCENT)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PURCHASE_PRICE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_SELL_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(Databases.COL_SELL_COUNT))
                )
                resultList.add(result)
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getDataInfo(position: Int): DataInfo? {
        val cursor: Cursor
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_DATA + "WHERE ")
        sb.append("id = '$position'")
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count == 1){
            cursor.moveToFirst()
            val result = DataInfo(cursor.getInt(0), //id
                cursor.getString(1), //subjectName
                cursor.getString(2), //realPainLossesAmount
                cursor.getString(3), //purchaseDate
                cursor.getString(4), //sellDate
                cursor.getString(5), //gainPercent
                cursor.getString(6), //purchasePrice
                cursor.getString(7), //sellPrice
                cursor.getInt(8)) //sellCount
            cursor?.close()
            return result
        }else{
            cursor?.close()
            return null
        }
    }

    fun getGainDataInfo(): ArrayList<DataInfo?>?{
        val cursor: Cursor
        val resultList = ArrayList<DataInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_DATA)
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count>0){
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                val realGainLossesAmount
                        = cursor.getString(cursor.getColumnIndex(Databases.COL_REAL_GAINS_LOSSES_AMOUNT))
                val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                if(realGainLossesAmountNum >= 0){
                    val result = DataInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PURCHASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_SELL_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_SELL_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Databases.COL_SELL_COUNT))
                    )
                    resultList.add(result)
                }
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getLossDataInfo(): ArrayList<DataInfo?>?{
        val cursor: Cursor
        val resultList = ArrayList<DataInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_DATA)
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count>0){
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                val realGainLossesAmount
                        = cursor.getString(cursor.getColumnIndex(Databases.COL_REAL_GAINS_LOSSES_AMOUNT))
                val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                if(realGainLossesAmountNum < 0){
                    val result = DataInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PURCHASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_SELL_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_SELL_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Databases.COL_SELL_COUNT))
                    )
                    resultList.add(result)
                }
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun deleteDataInfo(position: Int){
//        val cursor: Cursor
//        val sb = StringBuilder()
//        sb.append("DELETE FROM " + Databases.TABLE_DATA + " WHERE ")
//        sb.append("id = $position")
//        database.rawQuery(sb.toString(), null)
//        cursor?.close()

//        database.use {
        database.delete(Databases.TABLE_DATA, "id = $position", null)
//        }
    }

}
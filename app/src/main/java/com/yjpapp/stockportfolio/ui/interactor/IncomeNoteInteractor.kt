package com.yjpapp.stockportfolio.ui.interactor

import android.content.ContentValues
import android.database.Cursor
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.util.ChoSungSearchQueryUtil
import com.yjpapp.stockportfolio.util.Utils

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yun Jae-park
 * @since 2020.12
 */
class IncomeNoteInteractor: BaseInteractor() {

//    companion object {
//        @Volatile private var instance: IncomeNoteInteractor? = null
//        private lateinit var mContext: Context
////        private lateinit var databaseController: DatabaseController
//        private lateinit var dbHelper: DatabaseOpenHelper
//        private lateinit var database: SQLiteDatabase
//        @JvmStatic
//        fun getInstance(context: Context): IncomeNoteInteractor =
//                instance ?: synchronized(this) {
//                    instance ?: IncomeNoteInteractor().also {
//                        instance = it
//                        mContext = context
//                        dbHelper = DatabaseOpenHelper(mContext)
//                        database = dbHelper.writableDatabase
//                    }
//                }
//
//    }
    fun insertIncomeNoteInfo(incomeNoteInfo: IncomeNoteInfo): Boolean{
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_INCOME_NOTE_SUBJECT_NAME, incomeNoteInfo.subjectName)
            put(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT, incomeNoteInfo.realPainLossesAmount)
            put(Databases.COL_INCOME_NOTE_PURCHASE_DATE, incomeNoteInfo.purchaseDate)
            put(Databases.COL_INCOME_NOTE_SELL_DATE, incomeNoteInfo.sellDate)
            put(Databases.COL_INCOME_NOTE_GAIN_PERCENT, incomeNoteInfo.gainPercent)
            put(Databases.COL_INCOME_NOTE_PURCHASE_PRICE, incomeNoteInfo.purchasePrice)
            put(Databases.COL_INCOME_NOTE_SELL_PRICE, incomeNoteInfo.sellPrice)
            put(Databases.COL_INCOME_NOTE_SELL_COUNT, incomeNoteInfo.sellCount)
        }

        insertCheck = database.insert(Databases.TABLE_INCOME_NOTE, null, contentValues)

        return insertCheck != -1L
    }

    fun updateIncomeNoteInfo(incomeNoteInfo: IncomeNoteInfo): Boolean{
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_INCOME_NOTE_SUBJECT_NAME, incomeNoteInfo.subjectName)
            put(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT, incomeNoteInfo.realPainLossesAmount)
            put(Databases.COL_INCOME_NOTE_PURCHASE_DATE, incomeNoteInfo.purchaseDate)
            put(Databases.COL_INCOME_NOTE_SELL_DATE, incomeNoteInfo.sellDate)
            put(Databases.COL_INCOME_NOTE_GAIN_PERCENT, incomeNoteInfo.gainPercent)
            put(Databases.COL_INCOME_NOTE_PURCHASE_PRICE, incomeNoteInfo.purchasePrice)
            put(Databases.COL_INCOME_NOTE_SELL_PRICE, incomeNoteInfo.sellPrice)
            put(Databases.COL_INCOME_NOTE_SELL_COUNT, incomeNoteInfo.sellCount)
        }

        updateCheck = database.update(Databases.TABLE_INCOME_NOTE, contentValues,
                Databases.COL_INCOME_NOTE_ID + " = ? ", arrayOf(incomeNoteInfo.id.toString()))

        return updateCheck != -1
    }

    fun deleteIncomeNoteInfo(id: Int){
        database.delete(Databases.TABLE_INCOME_NOTE, "id = $id", null)
//        databaseController.deleteData(id, Databases.TABLE_INCOME_NOTE)
    }

    fun getAllIncomeNoteInfoList(): ArrayList<IncomeNoteInfo?>{
        val cursor: Cursor
        val resultList = ArrayList<IncomeNoteInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_INCOME_NOTE)
        cursor = database.rawQuery(sb.toString(), null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                val result = IncomeNoteInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_COUNT))
                )
                resultList.add(result)
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getGainIncomeNoteInfoList(): ArrayList<IncomeNoteInfo?>{
        val cursor: Cursor
        val resultList = ArrayList<IncomeNoteInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_INCOME_NOTE)
        cursor = database.rawQuery(sb.toString(), null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                val realGainLossesAmount = cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT))
                val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                if (realGainLossesAmountNum >= 0) {
                    val result = IncomeNoteInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_ID)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SUBJECT_NAME)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_DATE)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_GAIN_PERCENT)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_PRICE)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_PRICE)),
                            cursor.getInt(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_COUNT))
                    )
                    resultList.add(result)
                }
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getLossIncomeNoteInfoList(): ArrayList<IncomeNoteInfo?>{
        val cursor: Cursor
        val resultList = ArrayList<IncomeNoteInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_INCOME_NOTE)
        cursor = database.rawQuery(sb.toString(), null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                val realGainLossesAmount = cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT))
                val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                if (realGainLossesAmountNum < 0) {
                    val result = IncomeNoteInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_ID)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SUBJECT_NAME)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_DATE)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_GAIN_PERCENT)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_PRICE)),
                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_PRICE)),
                            cursor.getInt(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_COUNT))
                    )
                    resultList.add(result)
                }
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getIncomeNoteInfo(position: Int): IncomeNoteInfo?{
        val id = getAllIncomeNoteInfoList()[position]!!.id
        val cursor: Cursor
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_INCOME_NOTE + " WHERE ")
        sb.append("id = '$id'")
        cursor = database.rawQuery(sb.toString(), null)
        if (cursor.count == 1) {
            cursor.moveToFirst()
            val result = IncomeNoteInfo(cursor.getInt(0), //id
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
        } else {
            cursor?.close()
            return null
        }
    }

    fun getSearchNoteList(newText: String?): MutableList<IncomeNoteInfo?>{
        val cursor: Cursor
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_INCOME_NOTE + " WHERE ")
        sb.append(Databases.COL_INCOME_NOTE_SUBJECT_NAME + " LIKE '%" + newText + "%' OR" + ChoSungSearchQueryUtil.makeQuery(newText, Databases.COL_INCOME_NOTE_SUBJECT_NAME)+ " ;")
//        val searchQuery = "SELECT * FROM ${Databases.TABLE_INCOME_NOTE} WHERE ${Databases.COL_INCOME_NOTE_SUBJECT_NAME} LIKE '%" + newText + "%' OR " + ChoSungSearchQueryUtil.makeQuery(newText) + " ;"
        val resultList:MutableList<IncomeNoteInfo?> = mutableListOf()

        cursor = database.rawQuery(sb.toString(), null)
        while(cursor.moveToNext()){
            val incomeNoteInfo = IncomeNoteInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_ID)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SUBJECT_NAME)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_DATE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_DATE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_GAIN_PERCENT)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_PRICE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_SELL_COUNT))
            )
            resultList.add(incomeNoteInfo)
        }

        cursor.close()
        return resultList
    }
}
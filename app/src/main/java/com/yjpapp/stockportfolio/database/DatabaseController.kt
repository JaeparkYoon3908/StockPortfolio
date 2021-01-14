package com.yjpapp.stockportfolio.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.database.data.MemoInfo
import com.yjpapp.stockportfolio.database.data.MyStockInfo
import com.yjpapp.stockportfolio.util.Utils

class DatabaseController {
    // 싱글톤 패턴 적용 완료
    companion object {
        @Volatile
        private var instance: DatabaseController? = null
        private lateinit var mContext: Context
        private lateinit var dbHelper: DatabaseOpenHelper
        private lateinit var database: SQLiteDatabase

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

    fun insertIncomeNoteData(incomeNoteInfo: IncomeNoteInfo?): Boolean {
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_INCOME_NOTE_SUBJECT_NAME, incomeNoteInfo?.subjectName)
            put(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT, incomeNoteInfo?.realPainLossesAmount)
            put(Databases.COL_INCOME_NOTE_PURCHASE_DATE, incomeNoteInfo?.purchaseDate)
            put(Databases.COL_INCOME_NOTE_SELL_DATE, incomeNoteInfo?.sellDate)
            put(Databases.COL_INCOME_NOTE_GAIN_PERCENT, incomeNoteInfo?.gainPercent)
            put(Databases.COL_INCOME_NOTE_PURCHASE_PRICE, incomeNoteInfo?.purchasePrice)
            put(Databases.COL_INCOME_NOTE_SELL_PRICE, incomeNoteInfo?.sellPrice)
            put(Databases.COL_INCOME_NOTE_SELL_COUNT, incomeNoteInfo?.sellCount)
        }

        insertCheck = database.insert(Databases.TABLE_INCOME_NOTE, null, contentValues)

        return insertCheck != -1L
    }

    fun updateIncomeNoteData(incomeNoteInfo: IncomeNoteInfo?): Boolean {
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_INCOME_NOTE_SUBJECT_NAME, incomeNoteInfo?.subjectName)
            put(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT, incomeNoteInfo?.realPainLossesAmount)
            put(Databases.COL_INCOME_NOTE_PURCHASE_DATE, incomeNoteInfo?.purchaseDate)
            put(Databases.COL_INCOME_NOTE_SELL_DATE, incomeNoteInfo?.sellDate)
            put(Databases.COL_INCOME_NOTE_GAIN_PERCENT, incomeNoteInfo?.gainPercent)
            put(Databases.COL_INCOME_NOTE_PURCHASE_PRICE, incomeNoteInfo?.purchasePrice)
            put(Databases.COL_INCOME_NOTE_SELL_PRICE, incomeNoteInfo?.sellPrice)
            put(Databases.COL_INCOME_NOTE_SELL_COUNT, incomeNoteInfo?.sellCount)
        }

        updateCheck = database.update(Databases.TABLE_INCOME_NOTE, contentValues,
                Databases.COL_INCOME_NOTE_ID + " = ? ", arrayOf(incomeNoteInfo?.id.toString()))

        return updateCheck != -1
    }

    fun getAllIncomeNoteList(): ArrayList<IncomeNoteInfo?> {
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

    fun getIncomeNoteLInfo(id: Int): IncomeNoteInfo? {
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

    fun getGainIncomeNoteList(): ArrayList<IncomeNoteInfo?> {
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

    fun getLossIncomeNoteList(): ArrayList<IncomeNoteInfo?> {
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

    //TODO 검색 기능 구현.
    fun getSearchBarIncomeNoteList(): ArrayList<IncomeNoteInfo?> {
        val cursor: Cursor
        val resultList = ArrayList<IncomeNoteInfo?>()
        return resultList
    }

    fun deleteData(id: Int, table: String) {
//        val cursor: Cursor
//        val sb = StringBuilder()
//        sb.append("DELETE FROM " + Databases.TABLE_DATA + " WHERE ")
//        sb.append("id = $position")
//        database.rawQuery(sb.toString(), null)
//        cursor?.close()

//        database.use {
        database.delete(table, "id = $id", null)
//        }
    }

    fun getAllMemoInfoList(): ArrayList<MemoInfo?> {
        val cursor: Cursor
        val resultList = ArrayList<MemoInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_MEMO)
        cursor = database.rawQuery(sb.toString(), null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                val result = MemoInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_MEMO_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MEMO_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MEMO_TITLE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MEMO_CONTENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MEMO_DELETE_CHECK))
                )
                resultList.add(result)
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getMemoInfo(id: Int): MemoInfo? {
        val cursor: Cursor
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_MEMO + " WHERE ")
        sb.append("id = '$id'")
        cursor = database.rawQuery(sb.toString(), null)
        return if (cursor.count == 1) {
            cursor.moveToFirst()
            val result = MemoInfo(cursor.getInt(0), //id
                    cursor.getString(1), //date
                    cursor.getString(2), //title
                    cursor.getString(3), //content
                    cursor.getString(4)) //deleteChecked
            cursor?.close()
            result
        } else {
            cursor?.close()
            null
        }
    }

    fun insertMemoData(memoInfo: MemoInfo?): Boolean {
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_MEMO_DATE, memoInfo?.date)
        contentValues.put(Databases.COL_MEMO_TITLE, memoInfo?.title)
        contentValues.put(Databases.COL_MEMO_CONTENT, memoInfo?.content)

        insertCheck = database.insert(Databases.TABLE_MEMO, null, contentValues)

        return insertCheck != -1L
    }

    fun updateMemoData(memoInfo: MemoInfo?): Boolean {
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_MEMO_DATE, memoInfo?.date)
        contentValues.put(Databases.COL_MEMO_TITLE, memoInfo?.title)
        contentValues.put(Databases.COL_MEMO_CONTENT, memoInfo?.content)

        updateCheck = database.update(Databases.TABLE_MEMO, contentValues,
                Databases.COL_MEMO_ID + " = ? ", arrayOf(memoInfo?.id.toString()))

        return updateCheck != -1
    }

    fun updateDeleteCheck(id: Int, deleteCheck: String): Boolean {
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_MEMO_DELETE_CHECK, deleteCheck)

        updateCheck = database.update(Databases.TABLE_MEMO, contentValues,
                Databases.COL_MEMO_ID + " = ? ", arrayOf(id.toString()))

        return updateCheck != -1
    }

    fun getAllMyStockList(): ArrayList<MyStockInfo?>{
        val cursor: Cursor
        val resultList = ArrayList<MyStockInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_MY_STOCK)
        cursor = database.rawQuery(sb.toString(), null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                val result = MyStockInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_MY_STOCK_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_CURRENT_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_COUNT))
                )
                resultList.add(result)
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun insertMyStockData(myStockInfo: MyStockInfo?): Boolean {
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_MY_STOCK_SUBJECT_NAME, myStockInfo?.subjectName)
            put(Databases.COL_MY_STOCK_REAL_GAINS_LOSSES_AMOUNT, myStockInfo?.realPainLossesAmount)
            put(Databases.COL_MY_STOCK_GAIN_PERCENT, myStockInfo?.gainPercent)
            put(Databases.COL_MY_STOCK_PURCHASE_DATE, myStockInfo?.purchaseDate)
            put(Databases.COL_MY_STOCK_PURCHASE_PRICE, myStockInfo?.purchasePrice)
            put(Databases.COL_MY_STOCK_CURRENT_PRICE, myStockInfo?.currentPrice)
            put(Databases.COL_MY_STOCK_PURCHASE_COUNT, myStockInfo?.purchaseCount)
        }

        insertCheck = database.insert(Databases.TABLE_MY_STOCK, null, contentValues)

        return insertCheck != -1L
    }

    fun updateMyStockData(myStockInfo: MyStockInfo?): Boolean {
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_MY_STOCK_SUBJECT_NAME, myStockInfo?.subjectName)
            put(Databases.COL_MY_STOCK_REAL_GAINS_LOSSES_AMOUNT, myStockInfo?.realPainLossesAmount)
            put(Databases.COL_MY_STOCK_GAIN_PERCENT, myStockInfo?.gainPercent)
            put(Databases.COL_MY_STOCK_PURCHASE_DATE, myStockInfo?.purchaseDate)
            put(Databases.COL_MY_STOCK_PURCHASE_PRICE, myStockInfo?.purchasePrice)
            put(Databases.COL_MY_STOCK_CURRENT_PRICE, myStockInfo?.currentPrice)
            put(Databases.COL_MY_STOCK_PURCHASE_COUNT, myStockInfo?.purchaseCount)
        }

        updateCheck = database.update(Databases.TABLE_MY_STOCK, contentValues,
                Databases.COL_MY_STOCK_ID + " = ? ", arrayOf(myStockInfo?.id.toString()))

        return updateCheck != -1
    }

    fun updateCurrentPrice(id: Int, currentPrice: String?): Boolean{
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_MY_STOCK_CURRENT_PRICE, currentPrice)
        }

        updateCheck = database.update(Databases.TABLE_MY_STOCK, contentValues,
                Databases.COL_MY_STOCK_ID + " = ? ", arrayOf(id.toString()))

        return updateCheck != -1
    }

    fun getGainMyStockList(): ArrayList<MyStockInfo?> {
        val cursor: Cursor
        val resultList = ArrayList<MyStockInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_MY_STOCK)
        cursor = database.rawQuery(sb.toString(), null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                val realGainLossesAmount = cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT))
                val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                if (realGainLossesAmountNum >= 0) {
                    val result = MyStockInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_MY_STOCK_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_CURRENT_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_COUNT)))
                    resultList.add(result)
                }
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getLossMyStockList(): ArrayList<MyStockInfo?> {
        val cursor: Cursor
        val resultList = ArrayList<MyStockInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_MY_STOCK)
        cursor = database.rawQuery(sb.toString(), null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            for (i in 0 until cursor.count) {
                val realGainLossesAmount = cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_REAL_GAINS_LOSSES_AMOUNT))
                val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                if (realGainLossesAmountNum < 0) {
                    val result = MyStockInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_MY_STOCK_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_MY_STOCK_CURRENT_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Databases.COL_MY_STOCK_PURCHASE_COUNT))
                    )
                    resultList.add(result)
                }
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }
}
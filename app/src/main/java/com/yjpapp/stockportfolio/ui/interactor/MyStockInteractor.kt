package com.yjpapp.stockportfolio.ui.interactor

import android.content.ContentValues
import android.database.Cursor
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.data.MyStockInfo

/**
 * MyStockFragment의 Model 역할하는 class
 *
 * @author Yun Jae-park
 * @since 2020.12
 */
class MyStockInteractor: BaseInteractor() {

//    companion object {
//        @Volatile private var instance: MyStockInteractor? = null
//        private lateinit var mContext: Context
//        private lateinit var databaseController: DatabaseController
//        @JvmStatic
//        fun getInstance(context: Context): MyStockInteractor =
//                instance ?: synchronized(this) {
//                    instance ?: MyStockInteractor().also {
//                        instance = it
//                        mContext = context
//                        databaseController = DatabaseController.getInstance(mContext)
//                    }
//                }
//    }

    fun getAllMyStockList(): ArrayList<MyStockInfo?> {
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

    fun insertMyStockInfo(myStockInfo: MyStockInfo): Boolean{
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_MY_STOCK_SUBJECT_NAME, myStockInfo.subjectName)
            put(Databases.COL_MY_STOCK_REAL_GAINS_LOSSES_AMOUNT, myStockInfo.realPainLossesAmount)
            put(Databases.COL_MY_STOCK_GAIN_PERCENT, myStockInfo.gainPercent)
            put(Databases.COL_MY_STOCK_PURCHASE_DATE, myStockInfo.purchaseDate)
            put(Databases.COL_MY_STOCK_PURCHASE_PRICE, myStockInfo.purchasePrice)
            put(Databases.COL_MY_STOCK_CURRENT_PRICE, myStockInfo.currentPrice)
            put(Databases.COL_MY_STOCK_PURCHASE_COUNT, myStockInfo.purchaseCount)
        }

        insertCheck = database.insert(Databases.TABLE_MY_STOCK, null, contentValues)

        return insertCheck != -1L
    }

    fun updateMyStockInfo(myStockInfo: MyStockInfo): Boolean{
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_MY_STOCK_SUBJECT_NAME, myStockInfo.subjectName)
            put(Databases.COL_MY_STOCK_REAL_GAINS_LOSSES_AMOUNT, myStockInfo.realPainLossesAmount)
            put(Databases.COL_MY_STOCK_GAIN_PERCENT, myStockInfo.gainPercent)
            put(Databases.COL_MY_STOCK_PURCHASE_DATE, myStockInfo.purchaseDate)
            put(Databases.COL_MY_STOCK_PURCHASE_PRICE, myStockInfo.purchasePrice)
            put(Databases.COL_MY_STOCK_CURRENT_PRICE, myStockInfo.currentPrice)
            put(Databases.COL_MY_STOCK_PURCHASE_COUNT, myStockInfo.purchaseCount)
        }

        updateCheck = database.update(Databases.TABLE_MY_STOCK, contentValues,
                Databases.COL_MY_STOCK_ID + " = ? ", arrayOf(myStockInfo.id.toString()))

        return updateCheck != -1
    }

    fun updateCurrentPrice(id: Int, currentPrice: String): Boolean{
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.apply {
            put(Databases.COL_MY_STOCK_CURRENT_PRICE, currentPrice)
        }

        updateCheck = database.update(Databases.TABLE_MY_STOCK, contentValues,
                Databases.COL_MY_STOCK_ID + " = ? ", arrayOf(id.toString()))

        return updateCheck != -1
    }

    fun deleteMyStockInfo(id: Int){
        database.delete(Databases.TABLE_MY_STOCK, "id = $id", null)
    }
}
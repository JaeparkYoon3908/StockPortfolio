package com.yjpapp.stockportfolio.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.yjpapp.stockportfolio.model.MemoInfo
import com.yjpapp.stockportfolio.model.PortfolioInfo
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

    fun insertPortfolioData(portfolioInfo: PortfolioInfo?): Boolean{
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_PORTFOLIO_SUBJECT_NAME,portfolioInfo?.subjectName)
        contentValues.put(Databases.COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT, portfolioInfo?.realPainLossesAmount)
        contentValues.put(Databases.COL_PORTFOLIO_PURCHASE_DATE, portfolioInfo?.purchaseDate)
        contentValues.put(Databases.COL_PORTFOLIO_SELL_DATE, portfolioInfo?.sellDate)
        contentValues.put(Databases.COL_PORTFOLIO_GAIN_PERCENT, portfolioInfo?.gainPercent)
        contentValues.put(Databases.COL_PORTFOLIO_PURCHASE_PRICE, portfolioInfo?.purchasePrice)
        contentValues.put(Databases.COL_PORTFOLIO_SELL_PRICE, portfolioInfo?.sellPrice)
        contentValues.put(Databases.COL_PORTFOLIO_SELL_COUNT, portfolioInfo?.sellCount)

        insertCheck = database.insert(Databases.TABLE_PORTFOLIO,null, contentValues)

        return insertCheck != -1L
    }

    fun updatePortfolioData(portfolioInfo: PortfolioInfo?): Boolean{
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_PORTFOLIO_SUBJECT_NAME,portfolioInfo?.subjectName)
        contentValues.put(Databases.COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT, portfolioInfo?.realPainLossesAmount)
        contentValues.put(Databases.COL_PORTFOLIO_PURCHASE_DATE, portfolioInfo?.purchaseDate)
        contentValues.put(Databases.COL_PORTFOLIO_SELL_DATE, portfolioInfo?.sellDate)
        contentValues.put(Databases.COL_PORTFOLIO_GAIN_PERCENT, portfolioInfo?.gainPercent)
        contentValues.put(Databases.COL_PORTFOLIO_PURCHASE_PRICE, portfolioInfo?.purchasePrice)
        contentValues.put(Databases.COL_PORTFOLIO_SELL_PRICE, portfolioInfo?.sellPrice)
        contentValues.put(Databases.COL_PORTFOLIO_SELL_COUNT, portfolioInfo?.sellCount)

        updateCheck = database.update(Databases.TABLE_PORTFOLIO, contentValues,
            Databases.COL_PORTFOLIO_ID + " = ? ", arrayOf(portfolioInfo?.id.toString()))

        return updateCheck != -1
    }

    fun getAllPortfolioDataInfo(): ArrayList<PortfolioInfo?> {
        val cursor: Cursor
        val resultList = ArrayList<PortfolioInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_PORTFOLIO)
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count>0){
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                val result = PortfolioInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_PORTFOLIO_ID)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SUBJECT_NAME)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_PURCHASE_DATE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_DATE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_GAIN_PERCENT)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_PURCHASE_PRICE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_COUNT))
                )
                resultList.add(result)
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getDataInfo(position: Int): PortfolioInfo? {
        val cursor: Cursor
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_PORTFOLIO + "WHERE ")
        sb.append("id = '$position'")
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count == 1){
            cursor.moveToFirst()
            val result = PortfolioInfo(cursor.getInt(0), //id
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

    fun getGainPortfolioInfo(): ArrayList<PortfolioInfo?>?{
        val cursor: Cursor
        val resultList = ArrayList<PortfolioInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_PORTFOLIO)
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count>0){
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                val realGainLossesAmount
                        = cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT))
                val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                if(realGainLossesAmountNum >= 0){
                    val result = PortfolioInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_PORTFOLIO_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_PURCHASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_COUNT))
                    )
                    resultList.add(result)
                }
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun getLossPortfolioInfo(): ArrayList<PortfolioInfo?>?{
        val cursor: Cursor
        val resultList = ArrayList<PortfolioInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_PORTFOLIO)
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count>0){
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                val realGainLossesAmount
                        = cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT))
                val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                if(realGainLossesAmountNum < 0){
                    val result = PortfolioInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_PORTFOLIO_ID)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SUBJECT_NAME)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_REAL_GAINS_LOSSES_AMOUNT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_PURCHASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_DATE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_GAIN_PERCENT)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_PURCHASE_PRICE)),
                        cursor.getString(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_PRICE)),
                        cursor.getInt(cursor.getColumnIndex(Databases.COL_PORTFOLIO_SELL_COUNT))
                    )
                    resultList.add(result)
                }
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

    fun deleteDataInfo(position: Int, table: String){
//        val cursor: Cursor
//        val sb = StringBuilder()
//        sb.append("DELETE FROM " + Databases.TABLE_DATA + " WHERE ")
//        sb.append("id = $position")
//        database.rawQuery(sb.toString(), null)
//        cursor?.close()

//        database.use {
        database.delete(table, "id = $position", null)
//        }
    }

    fun insertMemoData(memoInfo: MemoInfo?): Boolean{
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_MEMO_DATE, memoInfo?.date)
        contentValues.put(Databases.COL_MEMO_TITLE,memoInfo?.title)
        contentValues.put(Databases.COL_MEMO_CONTENT, memoInfo?.content)

        insertCheck = database.insert(Databases.TABLE_MEMO,null, contentValues)

        return insertCheck != -1L
    }

    fun getAllMemoDataInfo(): ArrayList<MemoInfo?> {
        val cursor: Cursor
        val resultList = ArrayList<MemoInfo?>()
        val sb = StringBuilder()
        sb.append("SELECT * FROM " + Databases.TABLE_MEMO)
        cursor = database.rawQuery(sb.toString(), null)
        if(cursor.count>0){
            cursor.moveToFirst()
            for(i in 0 until cursor.count){
                val result = MemoInfo(cursor.getInt(cursor.getColumnIndex(Databases.COL_MEMO_ID)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_MEMO_DATE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_MEMO_TITLE)),
                    cursor.getString(cursor.getColumnIndex(Databases.COL_MEMO_CONTENT))
                )
                resultList.add(result)
                cursor.moveToNext()
            }
        }
        cursor?.close()
        return resultList
    }

}
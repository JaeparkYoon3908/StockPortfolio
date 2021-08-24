package com.yjpapp.stockportfolio.function.incomenote

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import androidx.paging.*
import com.yjpapp.stockportfolio.localdb.sqlte.Databases
import com.yjpapp.stockportfolio.localdb.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.base.BaseInteractor
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
import com.yjpapp.stockportfolio.network.RetrofitClient
import com.yjpapp.stockportfolio.util.ChoSungSearchQueryUtil
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.coroutines.flow.Flow

/**
 * IncomeNoteFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
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
//            put(Databases.COL_INCOME_NOTE_PURCHASE_DATE, incomeNoteInfo.purchaseDate)
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
//            put(Databases.COL_INCOME_NOTE_PURCHASE_DATE, incomeNoteInfo.purchaseDate)
            put(Databases.COL_INCOME_NOTE_SELL_DATE, incomeNoteInfo.sellDate)
            put(Databases.COL_INCOME_NOTE_GAIN_PERCENT, incomeNoteInfo.gainPercent)
            put(Databases.COL_INCOME_NOTE_PURCHASE_PRICE, incomeNoteInfo.purchasePrice)
            put(Databases.COL_INCOME_NOTE_SELL_PRICE, incomeNoteInfo.sellPrice)
            put(Databases.COL_INCOME_NOTE_SELL_COUNT, incomeNoteInfo.sellCount)
        }

        updateCheck = database.update(
            Databases.TABLE_INCOME_NOTE, contentValues,
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
//                        cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_DATE)),
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
//                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_DATE)),
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
//                            cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_DATE)),
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
//                    cursor.getString(3), //purchaseDate
                    cursor.getString(3), //sellDate
                    cursor.getString(4), //gainPercent
                    cursor.getString(5), //purchasePrice
                    cursor.getString(6), //sellPrice
                    cursor.getInt(7)) //sellCount
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
//                    cursor.getString(cursor.getColumnIndex(Databases.COL_INCOME_NOTE_PURCHASE_DATE)),
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

    //TODO 네트워크로 전환 예정
    suspend fun requestPostIncomeNote(context: Context, respIncomeNoteInfo: RespIncomeNoteInfo.IncomeNoteList?) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestPostIncomeNote(respIncomeNoteInfo)

    suspend fun requestDeleteIncomeNote(context: Context, id: Int) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestDeleteIncomeNote(id)


    suspend fun requestPutIncomeNote(context: Context, respIncomeNoteList: RespIncomeNoteInfo.IncomeNoteList?) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestPutIncomeNote(respIncomeNoteList)

    suspend fun requestGetIncomeNote(context: Context, params: HashMap<String, String>) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestGetIncomeNote(params)

    inner class IncomeNotePagingSource(val context: Context, var startDate: String, var endDate: String): PagingSource<Int, RespIncomeNoteInfo.IncomeNoteList>() {
        private val pageSize = "20"
        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RespIncomeNoteInfo.IncomeNoteList> {
            val page = params.key ?: 1
            return try {
                //TODO 코드 개선하기.
                val hashMap = HashMap<String, String>()
                hashMap["page"] = page.toString()
                hashMap["size"] = pageSize
                hashMap["startDate"] = startDate
                hashMap["endDate"] = endDate
                val data = requestGetIncomeNote(context, hashMap)
                LoadResult.Page(
                    data = data?.body()?.income_note!!,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = if (data.body()?.income_note?.isEmpty()!!) null else page + 1)
            } catch (e: Exception) {
                return LoadResult.Error(e)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, RespIncomeNoteInfo.IncomeNoteList>): Int? {
//            return state.anchorPosition?.let { anchorPosition ->
//                state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
//                    ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
//            }
            return 1
        }
    }

    fun getIncomeNoteListByPaging(context: Context, startDate: String, endDate: String): Flow<PagingData<RespIncomeNoteInfo.IncomeNoteList>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { IncomeNotePagingSource(context, startDate, endDate) }).flow
    }
}
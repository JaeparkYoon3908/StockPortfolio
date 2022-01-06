package com.yjpapp.stockportfolio.function.memo.detail

import android.content.ContentValues
import android.content.Context
import com.yjpapp.stockportfolio.localdb.sqlte.Databases
import com.yjpapp.stockportfolio.localdb.sqlte.data.MemoInfo
import com.yjpapp.stockportfolio.base.BaseInteractor
import com.yjpapp.stockportfolio.localdb.sqlte.DatabaseOpenHelper

/**
 * MemoReadWriteActivity의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
class MemoReadWriteRepository(context: Context) {
    private val dbHelper = DatabaseOpenHelper(context)
    private val database = dbHelper.writableDatabase

    fun insertMemoData(memoData: MemoInfo): Boolean{
        val insertCheck: Long
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_MEMO_DATE, memoData.date)
        contentValues.put(Databases.COL_MEMO_TITLE, memoData.title)
        contentValues.put(Databases.COL_MEMO_CONTENT, memoData.content)

        insertCheck = database.insert(Databases.TABLE_MEMO, null, contentValues)

        return insertCheck != -1L
    }
    fun updateMemoData(memoData: MemoInfo): Boolean{
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_MEMO_DATE, memoData.date)
        contentValues.put(Databases.COL_MEMO_TITLE, memoData.title)
        contentValues.put(Databases.COL_MEMO_CONTENT, memoData.content)

        updateCheck = database.update(
            Databases.TABLE_MEMO, contentValues,
                Databases.COL_MEMO_ID + " = ? ", arrayOf(memoData.id.toString()))

        return updateCheck != -1
    }
}
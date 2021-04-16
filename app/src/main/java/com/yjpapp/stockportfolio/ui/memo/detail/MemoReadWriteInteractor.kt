package com.yjpapp.stockportfolio.ui.memo.detail

import android.content.ContentValues
import com.yjpapp.stockportfolio.database.sqlte.Databases
import com.yjpapp.stockportfolio.database.sqlte.data.MemoInfo
import com.yjpapp.stockportfolio.base.BaseInteractor

/**
 * MemoReadWriteActivity의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
class MemoReadWriteInteractor: BaseInteractor() {

//    companion object {
//        @Volatile private var instance: MemoReadWriteInteractor? = null
////        private lateinit var mContext: Context
////        private lateinit var databaseController: DatabaseController
////        private lateinit var dbHelper: DatabaseOpenHelper
//        private lateinit var database: SQLiteDatabase
//        @JvmStatic
//        fun getInstance(context: Context): MemoReadWriteInteractor =
//                instance ?: synchronized(this) {
//                    instance ?: MemoReadWriteInteractor().also {
//                        instance = it
//                        mContext = context
////                        databaseController = DatabaseController.getInstance(mContext)
//                        dbHelper = DatabaseOpenHelper(mContext)
//                        database = MemoListInteractor.dbHelper.writableDatabase
//                    }
//                }
//
//    }
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
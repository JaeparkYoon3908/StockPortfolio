package com.yjpapp.stockportfolio.ui.interactor

import android.content.ContentValues
import android.database.Cursor
import com.yjpapp.stockportfolio.database.Databases
import com.yjpapp.stockportfolio.database.data.MemoInfo

/**
 * MemoListFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
class MemoListInteractor: BaseInteractor() {

//    companion object {
//        @Volatile private var instance: MemoListInteractor? = null
//        private lateinit var mContext: Context
////        private lateinit var databaseController: DatabaseController
//        private lateinit var dbHelper: DatabaseOpenHelper
//        private lateinit var database: SQLiteDatabase
//        @JvmStatic
//        fun getInstance(context: Context): MemoListInteractor =
//                instance ?: synchronized(this) {
//                    instance ?: MemoListInteractor().also {
//                        instance = it
//                        mContext = context
////                        databaseController = DatabaseController.getInstance(mContext)
//                        dbHelper = DatabaseOpenHelper(mContext)
//                        database = dbHelper.writableDatabase
//                    }
//                }
//
//    }

    fun getMemoInfo(id: Int): MemoInfo?{
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

    fun getAllMemoInfoList(): ArrayList<MemoInfo?>{
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

    fun updateDeleteCheck(id: Int, deleteCheck: String): Boolean{
        val updateCheck: Int
        val contentValues = ContentValues()
        contentValues.put(Databases.COL_MEMO_DELETE_CHECK, deleteCheck)

        updateCheck = database.update(Databases.TABLE_MEMO, contentValues,
                Databases.COL_MEMO_ID + " = ? ", arrayOf(id.toString()))

        return updateCheck != -1
    }

    fun deleteMemoInfoList(id: Int){
        database.delete(Databases.TABLE_MEMO, "id = $id", null)
//        databaseController.deleteData(id, Databases.TABLE_MEMO)
    }
}
package com.yjpapp.stockportfolio.function.memo.detail

import android.content.ContentValues
import android.content.Context
import com.yjpapp.stockportfolio.localdb.sqlte.Databases
import com.yjpapp.stockportfolio.localdb.sqlte.data.MemoInfo
import com.yjpapp.stockportfolio.base.BaseInteractor
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListDao
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.localdb.sqlte.DatabaseOpenHelper

/**
 * MemoReadWriteActivity의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
class MemoReadWriteRepository(
    private val memoListDao: MemoListDao
) {

    fun insertMemoData(memoData: MemoListEntity) {
        try {
            memoListDao.insert(memoData)
        } catch (e: Exception) {
            e.stackTrace
        }
    }
    fun updateMemoData(memoData: MemoListEntity) {
        try {
            memoListDao.update(memoData)
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}
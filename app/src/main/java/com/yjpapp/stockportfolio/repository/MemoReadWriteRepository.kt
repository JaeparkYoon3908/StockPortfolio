package com.yjpapp.stockportfolio.repository

import com.yjpapp.stockportfolio.localdb.room.memo.MemoListDao
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity

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
    fun deleteMomoData(id: Int) {
        try {
            memoListDao.deleteMemoInfo(id)
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}
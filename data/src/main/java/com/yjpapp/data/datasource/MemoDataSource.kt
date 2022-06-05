package com.yjpapp.data.datasource

import com.yjpapp.data.localdb.room.memo.MemoListDao
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import javax.inject.Inject

/**
 * MemoListFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class MemoDataSource @Inject constructor(
    private val memoListDao: MemoListDao
) {
    fun requestInsertMemoData(memoData: MemoListEntity) {
        try {
            memoListDao.insert(memoData)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun requestUpdateMemoData(memoData: MemoListEntity) {
        try {
            memoListDao.update(memoData)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun requestDeleteMomoData(id: Int): Boolean {
        return try {
            memoListDao.deleteMemoInfo(id)
            true
        } catch (e: Exception) {
            e.stackTrace
            false
        }
    }

    fun requestGetMemoInfo(id: Int): MemoListEntity {
       return memoListDao.getMemoInfo(id)
    }

    fun requestGetAllMemoInfoList(): MutableList<MemoListEntity> {
        return try {
            memoListDao.getAll()
        } catch (e: Exception) {
            e.stackTrace
            mutableListOf()
        }
    }

    fun requestUpdateDeleteCheck(id: Int, deleteCheck: String) {
        try {
            memoListDao.updateDeleteChecked(id, deleteCheck)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun requestDeleteMemoInfoList(memoListEntity: MemoListEntity){
        try {
            memoListDao.delete(memoListEntity)
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}
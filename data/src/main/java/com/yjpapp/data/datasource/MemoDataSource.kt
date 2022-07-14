package com.yjpapp.data.datasource

import com.yjpapp.data.localdb.room.memo.MemoDao
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import javax.inject.Inject

/**
 * MemoListFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class MemoDataSource @Inject constructor(
    private val memoDao: MemoDao
) {
    fun requestInsertMemoData(memoData: MemoListEntity) {
        try {
            memoDao.insert(memoData)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun requestUpdateMemoData(memoData: MemoListEntity) {
        try {
            memoDao.update(memoData)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun requestDeleteMomoData(id: Int): Boolean {
        return try {
            memoDao.deleteMemoInfo(id)
            true
        } catch (e: Exception) {
            e.stackTrace
            false
        }
    }

    fun requestGetMemoInfo(id: Int): MemoListEntity {
       return memoDao.getMemoInfo(id)
    }

    fun requestGetAllMemoInfoList(): MutableList<MemoListEntity> {
        return try {
            memoDao.getAll()
        } catch (e: Exception) {
            e.stackTrace
            mutableListOf()
        }
    }

    fun requestUpdateDeleteCheck(id: Int, deleteCheck: String) {
        try {
            memoDao.updateDeleteChecked(id, deleteCheck)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun requestDeleteMemoInfoList(memoListEntity: MemoListEntity){
        try {
            memoDao.delete(memoListEntity)
        } catch (e: Exception) {
            e.stackTrace
        }
    }
}
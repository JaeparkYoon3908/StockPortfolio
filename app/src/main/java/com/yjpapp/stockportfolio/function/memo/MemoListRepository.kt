package com.yjpapp.stockportfolio.function.memo

import com.yjpapp.stockportfolio.localdb.room.memo.MemoListDao
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity

/**
 * MemoListFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
class MemoListRepository(
    private val memoListDao: MemoListDao
) {

    fun getMemoInfo(id: Int): MemoListEntity{
       return memoListDao.getMemoInfo(id)
    }

    fun getAllMemoInfoList(): MutableList<MemoListEntity>{
        return memoListDao.getAll()
    }

    fun updateDeleteCheck(id: Int, deleteCheck: String){
        memoListDao.updateDeleteChecked(id, deleteCheck)
    }

    fun deleteMemoInfoList(memoListEntity: MemoListEntity){
        memoListDao.delete(memoListEntity)
    }
}
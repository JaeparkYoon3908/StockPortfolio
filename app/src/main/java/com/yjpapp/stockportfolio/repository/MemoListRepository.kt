package com.yjpapp.stockportfolio.repository

import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListDao
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity

/**
 * MemoListFragment의 Model 역할하는 class
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */
class MemoListRepository(
    private val memoListDao: MemoListDao,
    private val preferenceController: PreferenceController
) {

    fun getMemoInfo(id: Int): MemoListEntity{
       return memoListDao.getMemoInfo(id)
    }

    fun getAllMemoInfoList(): MutableList<MemoListEntity>{
        return try {
            memoListDao.getAll()
        } catch (e: Exception) {
            e.stackTrace
            mutableListOf()
        }
    }

    fun updateDeleteCheck(id: Int, deleteCheck: String){
        try {
            memoListDao.updateDeleteChecked(id, deleteCheck)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun deleteMemoInfoList(memoListEntity: MemoListEntity){
        try {
            memoListDao.delete(memoListEntity)
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun getIsMemoVibration(): Boolean {
        val isMemoVibration = preferenceController.getPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)?: ""
        return isMemoVibration == "true"
    }
}
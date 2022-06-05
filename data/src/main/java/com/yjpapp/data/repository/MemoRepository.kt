package com.yjpapp.data.repository

import com.yjpapp.data.datasource.MemoDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import javax.inject.Inject

class MemoRepository @Inject constructor(
    private val memoDataSource: MemoDataSource,
    private val preferenceDataSource: PreferenceDataSource
): BaseRepository()
{
    fun addMemoData(memoData: MemoListEntity) {
        memoDataSource.requestInsertMemoData(memoData)
    }

    fun modifyMemoData(memoData: MemoListEntity) {
        memoDataSource.requestUpdateMemoData(memoData)
    }

    fun deleteMemoData(id: Int) {
        memoDataSource.requestDeleteMomoData(id)
    }

    fun getMemoData(id: Int) = memoDataSource.requestGetMemoInfo(id)

    fun getAllMemoDataList() = memoDataSource.requestGetAllMemoInfoList()

    fun modifyDeleteCheck(id: Int, deleteCheck: String) {
        memoDataSource.requestUpdateDeleteCheck(id, deleteCheck)
    }

    fun deleteMemoInfoList(memoListEntity: MemoListEntity) {
        memoDataSource.requestDeleteMemoInfoList(memoListEntity)
    }

    fun getIsMemoVibration(): Boolean {
        val isMemoVibration = preferenceDataSource.getPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)?: ""
        return isMemoVibration == "true"
    }
}
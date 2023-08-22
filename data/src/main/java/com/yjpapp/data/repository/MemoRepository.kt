package com.yjpapp.data.repository

import com.yjpapp.data.datasource.MemoDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import javax.inject.Inject

interface MemoRepository {
    fun addMemoData(memoData: MemoListEntity)
    fun modifyMemoData(memoData: MemoListEntity)
    fun deleteMemoData(id: Int)
    fun getMemoData(id: Int): MemoListEntity
    fun getAllMemoDataList(): MutableList<MemoListEntity>
    fun modifyDeleteCheck(id: Int, deleteCheck: String)
    fun deleteMemoInfoList(memoListEntity: MemoListEntity)
    fun getIsMemoVibration(): Boolean
}
class MemoRepositoryImpl @Inject constructor(
    private val memoDataSource: MemoDataSource,
    private val preferenceDataSource: PreferenceDataSource
): MemoRepository {
    override fun addMemoData(memoData: MemoListEntity) {
        memoDataSource.requestInsertMemoData(memoData)
    }

    override fun modifyMemoData(memoData: MemoListEntity) {
        memoDataSource.requestUpdateMemoData(memoData)
    }

    override fun deleteMemoData(id: Int) {
        memoDataSource.requestDeleteMomoData(id)
    }

    override fun getMemoData(id: Int) = memoDataSource.requestGetMemoInfo(id)

    override fun getAllMemoDataList() = memoDataSource.requestGetAllMemoInfoList()

    override fun modifyDeleteCheck(id: Int, deleteCheck: String) {
        memoDataSource.requestUpdateDeleteCheck(id, deleteCheck)
    }

    override fun deleteMemoInfoList(memoListEntity: MemoListEntity) {
        memoDataSource.requestDeleteMemoInfoList(memoListEntity)
    }

    override fun getIsMemoVibration(): Boolean {
        val isMemoVibration = preferenceDataSource.getPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)?: ""
        return isMemoVibration == "true"
    }
}
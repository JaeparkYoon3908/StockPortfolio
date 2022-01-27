package com.yjpapp.stockportfolio.function.memo.detail

import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.repository.MemoRepository
import com.yjpapp.stockportfolio.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * since 2022.01.05
 * Presenter -> ViewModel 형식으로 변경
 */
@HiltViewModel
class MemoReadWriteViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    var mode: String? = null
    var memoListPosition = 0
    var id = 0
    var savedTitle: String = ""
    var savedContent: String = ""

    fun requestAddMemoData(date: String, title: String, content: String){
        val memoInfo = MemoListEntity(0, date, title, content, "false")
        memoRepository.insertMemoData(memoInfo)
    }

    fun requestUpdateMemoData(date: String, title: String, content: String){
        val memoInfo = MemoListEntity(id, date, title, content, "false")
        memoRepository.updateMemoData(memoInfo)
    }

    fun requestDeleteMemoData() {
        memoRepository.deleteMomoData(id)
    }

    fun requestSetPreference(prefKey: String, value: String) {
        preferenceRepository.setPreference(prefKey, value)
    }

    fun requestGetPreference(prefKey: String): String {
        return preferenceRepository.getPreference(prefKey)?: ""
    }

    fun requestIsExistPreference(prefKey: String): Boolean {
        return preferenceRepository.isExists(prefKey)
    }
}
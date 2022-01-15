package com.yjpapp.stockportfolio.function.memo.detail

import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.repository.MemoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * since 2022.01.05
 * Presenter -> ViewModel 형식으로 변경
 */
@HiltViewModel
class MemoReadWriteViewModel @Inject constructor(
    private val memoRepository: MemoRepository
) : ViewModel() {
    fun requestAddMemoData(date: String, title: String, content: String){
        val memoInfo = MemoListEntity(0, date, title, content, "false")
        memoRepository.insertMemoData(memoInfo)
    }

    fun requestUpdateMemoData(id: Int, date: String, title: String, content: String){
        val memoInfo = MemoListEntity(id, date, title, content, "false")
        memoRepository.updateMemoData(memoInfo)
    }
    fun requestDeleteMemoData(id: Int) {
        memoRepository.deleteMomoData(id)
    }
}
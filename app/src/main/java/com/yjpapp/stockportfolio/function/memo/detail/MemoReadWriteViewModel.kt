package com.yjpapp.stockportfolio.function.memo.detail

import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.localdb.sqlte.data.MemoInfo

/**
 * since 2022.01.05
 * Presenter -> ViewModel 형식으로 변경
 */
class MemoReadWriteViewModel(
    private val memoReadWriteRepository: MemoReadWriteRepository
) : ViewModel() {
    fun requestAddMemoData(date: String, title: String, content: String){
        val memoInfo = MemoInfo(0, date, title, content, "false")
        memoReadWriteRepository.insertMemoData(memoInfo)
    }

    fun requestUpdateMemoData(id: Int, date: String, title: String, content: String){
        val memoInfo = MemoInfo(id, date, title, content, "false")
        memoReadWriteRepository.updateMemoData(memoInfo)
    }
}
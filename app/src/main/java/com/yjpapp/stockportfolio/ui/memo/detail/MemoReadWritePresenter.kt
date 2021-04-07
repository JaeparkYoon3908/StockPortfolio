package com.yjpapp.stockportfolio.ui.memo.detail

import android.content.Context
import com.yjpapp.stockportfolio.database.data.MemoInfo

/**
 * MemoReadWriteActivity의 Presenter
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class MemoReadWritePresenter(val mContext: Context, private val memoReadWriteView: MemoReadWriteView) {
    private val memoReadWriteInteractor = MemoReadWriteInteractor()

    fun requestAddMemoData(date: String, title: String, content: String){
        val memoInfo = MemoInfo(0, date, title, content, "false")
        memoReadWriteInteractor.insertMemoData(memoInfo)
    }

    fun requestUpdateMemoData(id: Int, date: String, title: String, content: String){
        val memoInfo = MemoInfo(id, date, title, content, "false")
        memoReadWriteInteractor.updateMemoData(memoInfo)
    }
}
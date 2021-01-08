package com.yjpapp.stockportfolio.ui.presenter

import android.content.Context
import com.yjpapp.stockportfolio.database.data.MemoInfo
import com.yjpapp.stockportfolio.ui.interactor.MemoReadWriteInteractor
import com.yjpapp.stockportfolio.ui.view.MemoReadWriteView

class MemoReadWritePresenter(val mContext: Context, private val memoReadWriteView: MemoReadWriteView) {
    private val memoReadWriteInteractor = MemoReadWriteInteractor.getInstance(mContext)

    fun requestAddMemoData(date: String, title: String, content: String){
        val memoInfo = MemoInfo(0, date, title, content, "false")
        memoReadWriteInteractor.insertMemoData(memoInfo)
    }

    fun requestUpdateMemoData(id: Int, date: String, title: String, content: String){
        val memoInfo = MemoInfo(id, date, title, content, "false")
        memoReadWriteInteractor.updateMemoData(memoInfo)
    }
}
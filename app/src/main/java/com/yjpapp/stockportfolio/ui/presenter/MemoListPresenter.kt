package com.yjpapp.stockportfolio.ui.presenter

import android.app.AlertDialog
import android.content.Context
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.MemoInfo
import com.yjpapp.stockportfolio.ui.interactor.MemoListInteractor
import com.yjpapp.stockportfolio.ui.view.MemoListView

class MemoListPresenter(val mContext: Context, private val memoListView: MemoListView) {
    

    private val memoListInteractor = MemoListInteractor.getInstance(mContext)

    fun onAddButtonClicked(){
        memoListView.startMemoReadWriteActivity()
    }
    fun onDeleteButtonClicked(){
        AlertDialog.Builder(mContext)
                .setMessage(mContext.getString(R.string.MemoListFragment_Delete_Check_Message))
                .setPositiveButton(R.string.Common_Ok) { _, _ ->
                    deleteMemoInfo()
                }
                .setNegativeButton(R.string.Common_Cancel) { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
    }

    fun addMemoInfo(){
        val memoDataList = memoListInteractor.getAllMemoInfoList()
        memoListView.addMemoListView(memoDataList)
        if (memoDataList.size != 0) {
            memoListView.hideGuideMessage()
        }
    }

    fun deleteMemoInfo(){
        val memoList = memoListInteractor.getAllMemoInfoList()
        for (position in memoList.indices) {
            if (memoList[position]?.deleteChecked!! == "true") {
                val id = memoList[position]!!.id
                memoListInteractor.deleteMemoInfoList(id)
                val updateMemoList = memoListInteractor.getAllMemoInfoList()
                memoListView.deleteMemoListView(updateMemoList, position)
            }
        }
        memoListView.setDeleteModeOff()
        if (memoList.size == 0) {
            memoListView.showGuideMessage()
        }
    }

    fun updateMemoInfo(){
        val memoList = memoListInteractor.getAllMemoInfoList()
        memoListView.updateMemoListView(memoList)
    }

    fun updateDeleteCheck(position: Int, deleteCheck: Boolean){
        val id = memoListInteractor.getAllMemoInfoList()[position]!!.id
        memoListInteractor.updateDeleteCheck(id, deleteCheck.toString())
    }

    fun getAllMemoInfoList(): ArrayList<MemoInfo?>{
       return memoListInteractor.getAllMemoInfoList()
    }
}
package com.yjpapp.stockportfolio.ui.presenter

import android.app.AlertDialog
import android.content.Context
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.DatabaseController
import com.yjpapp.stockportfolio.database.Databases
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
                    val memoList = memoListInteractor.getAllMemoInfoList()
                    for (i in memoList.indices) {
                        if (memoList[i]?.deleteChecked!! == "true") {
                            DatabaseController.getInstance(mContext).deleteData(memoList[i]?.id!!,
                                    Databases.TABLE_MEMO)
                            val newMemoList = DatabaseController.getInstance(mContext).getAllMemoInfoList()
                            memoListView.deleteMemoListView(newMemoList, i)
                        }
                    }
                    memoListView.setDeleteModeOff()
                    if (memoList.size == 0) {
                        memoListView.showGuideMessage()
                    }

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

    fun deleteMemoInfo(position: Int){
        val memoDataList = memoListInteractor.getAllMemoInfoList()
        memoListView.deleteMemoListView(memoDataList, position)
    }

    fun updateMemoInfo(){
        val memoDataList = memoListInteractor.getAllMemoInfoList()
        memoListView.updateMemoListView(memoDataList)
    }
}
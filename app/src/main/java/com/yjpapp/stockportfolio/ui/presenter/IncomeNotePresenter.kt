package com.yjpapp.stockportfolio.ui.presenter

import android.app.Activity
import android.content.Context
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.ui.adapter.IncomeNoteListAdapter
import com.yjpapp.stockportfolio.ui.interactor.IncomeNoteInteractor
import com.yjpapp.stockportfolio.ui.view.IncomeNoteView
import com.yjpapp.stockportfolio.util.Utils

/**
 * IncomeNoteFragment의 Presenter
 *
 * @author Yun Jae-park
 * @since 2020.12
 */

class IncomeNotePresenter(val mContext: Context, private val incomeNoteView: IncomeNoteView) {
    private var editMode = false
    private var incomeNoteId = -1
    private val incomeNoteInteractor = IncomeNoteInteractor.getInstance(mContext)
    private lateinit var incomeNoteListAdapter: IncomeNoteListAdapter
    fun onResume() {
        val allIncomeNoteInfo = incomeNoteInteractor.getAllIncomeNoteInfoList()
        incomeNoteListAdapter = IncomeNoteListAdapter(allIncomeNoteInfo, this)
        incomeNoteView.setAdapter(incomeNoteListAdapter)
    }

    fun onAllFilterClicked() {
        val allDataList = incomeNoteInteractor.getAllIncomeNoteInfoList()
        incomeNoteListAdapter.setDataInfoList(allDataList)
        incomeNoteListAdapter.notifyDataSetChanged()
        val text = mContext.getString(R.string.Common_All)
        incomeNoteView.changeFilterText(text)
    }

    fun onGainFilterClicked() {
        val gainDataList = incomeNoteInteractor.getGainIncomeNoteInfoList()
        incomeNoteListAdapter.setDataInfoList(gainDataList)
        incomeNoteListAdapter.notifyDataSetChanged()
        val text = mContext.getString(R.string.Common_Gain)
        incomeNoteView.changeFilterText(text)
    }

    fun onLossFilterClicked() {
        val lossDataList = incomeNoteInteractor.getLossIncomeNoteInfoList()
        incomeNoteListAdapter.setDataInfoList(lossDataList)
        incomeNoteListAdapter.notifyDataSetChanged()
        val text = mContext.getString(R.string.Common_Loss)
        incomeNoteView.changeFilterText(text)
    }

    fun onAddButtonClicked() {
        editMode = false
        incomeNoteId = -1
        incomeNoteView.showInputDialog(editMode, null)
    }

    fun onInputDialogCompleteClicked(incomeNoteInfo: IncomeNoteInfo) {
        //id 설정
        incomeNoteInfo.id = incomeNoteId

        if (editMode) {
            incomeNoteInteractor.updateIncomeNoteInfo(incomeNoteInfo)
            val newDataList = incomeNoteInteractor.getAllIncomeNoteInfoList()
            incomeNoteListAdapter.setDataInfoList(newDataList)
            incomeNoteListAdapter.notifyDataSetChanged()
            incomeNoteView.showAddButton()
            incomeNoteView.scrollTopPosition(newDataList.size - 1)
            incomeNoteView.bindTotalGainData()
        } else {
            incomeNoteInteractor.insertIncomeNoteInfo(incomeNoteInfo)
            val newDataList = incomeNoteInteractor.getAllIncomeNoteInfoList()
            incomeNoteListAdapter.setDataInfoList(newDataList)
            incomeNoteListAdapter.notifyItemInserted(incomeNoteListAdapter.itemCount - 1)
            incomeNoteView.scrollTopPosition(newDataList.size - 1)
            incomeNoteView.showAddButton()
            incomeNoteView.bindTotalGainData()
        }
    }

    fun onAdapterItemLongClick(isEditMode: Boolean) {
        if (isEditMode) {
            incomeNoteView.hideAddButton()
        } else {
            incomeNoteView.showAddButton()
        }
    }

    fun onEditButtonClick(position: Int) {
        editMode = true
        incomeNoteView.showAddButton()
        incomeNoteId = incomeNoteInteractor.getAllIncomeNoteInfoList()[position]!!.id
        val incomeNoteInfo = IncomeNoteInteractor.getInstance(mContext).getIncomeNoteInfo(position)
        incomeNoteView.showInputDialog(editMode, incomeNoteInfo!!)
    }

    fun onDeleteButtonClick(id: Int) {
        incomeNoteInteractor.deleteIncomeNoteInfo(id)
        val allIncomeNoteInfo = incomeNoteInteractor.getAllIncomeNoteInfoList()
        incomeNoteView.bindTotalGainData()
        if (allIncomeNoteInfo.size == 0) {
            incomeNoteView.showAddButton()
        }
    }

    fun onBackPressedClick(activity: Activity) {
        if (incomeNoteListAdapter.isEditMode()) {
            incomeNoteListAdapter.setEditMode(false)
            incomeNoteListAdapter.notifyDataSetChanged()
            incomeNoteView.showAddButton()
        } else {
            Utils.runBackPressAppCloseEvent(mContext, activity)
        }
    }

    fun onMenuEditButtonClick() {
        val allIncomeNoteList = incomeNoteInteractor.getAllIncomeNoteInfoList()
        if (allIncomeNoteList.size > 0) {
            incomeNoteListAdapter.setEditMode(!incomeNoteListAdapter.isEditMode())
            incomeNoteListAdapter.notifyDataSetChanged()
            if (incomeNoteListAdapter.isEditMode()) {
                incomeNoteView.hideAddButton()
            } else {
                incomeNoteView.showAddButton()
            }
        }
    }

    fun getAllIncomeNoteList(): ArrayList<IncomeNoteInfo?> {
        return incomeNoteInteractor.getAllIncomeNoteInfoList()
    }

//    fun onAdapterItemClick() {
//
//    }
    fun getEditMode(): Boolean{
        return editMode
    }

    fun onStartSearch(newText: String?){
        if(newText?.length!!>0){
            val searchIncomeNoteList = incomeNoteInteractor.getSearchNoteList(newText)
            incomeNoteListAdapter.setDataInfoList(searchIncomeNoteList)
        }else{
            val allIncomeNoteList = incomeNoteInteractor.getAllIncomeNoteInfoList()
            incomeNoteListAdapter.setDataInfoList(allIncomeNoteList)
        }
        incomeNoteListAdapter.notifyDataSetChanged()
    }
}
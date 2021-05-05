package com.yjpapp.stockportfolio.ui.incomenote

import android.app.Activity
import android.content.Context
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.database.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.util.Utils
import java.util.concurrent.CopyOnWriteArrayList

/**
 * IncomeNoteFragment의 Presenter
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class IncomeNotePresenter(val mContext: Context, private val incomeNoteView: IncomeNoteView) {
    private val FILTER_TYPE_ALL = 0
    private val FILTER_TYPE_GAIN = 1
    private val FILTER_TYPE_LOSS = 2
    private var filterType: Int = FILTER_TYPE_ALL
    private var filterText = mContext.getString(R.string.Common_All)
    private var searchText: String? = null
    private var editMode = false
    private var incomeNoteId = -1
    private val incomeNoteInteractor = IncomeNoteInteractor()
    private var incomeNoteListAdapter: IncomeNoteListAdapter? = null

    fun onResume() {
        val allIncomeNoteInfo = incomeNoteInteractor.getAllIncomeNoteInfoList()
        incomeNoteListAdapter = IncomeNoteListAdapter(allIncomeNoteInfo, this)
        incomeNoteView.setAdapter(incomeNoteListAdapter)
    }

    fun onAllFilterClicked() {
        filterType = FILTER_TYPE_ALL
        filterText = mContext.getString(R.string.Common_All)
        incomeNoteView.changeFilterText(filterText)
        if (searchText == null || searchText?.length == 0) {
            val allDataList = incomeNoteInteractor.getAllIncomeNoteInfoList()
            incomeNoteListAdapter?.setDataInfoList(allDataList)
            incomeNoteListAdapter?.notifyDataSetChanged()
        } else {
            onStartSearch(searchText)
        }
    }

    fun onGainFilterClicked() {
        filterType = FILTER_TYPE_GAIN
        filterText = mContext.getString(R.string.Common_Gain)
        incomeNoteView.changeFilterText(filterText)
        if (searchText == null || searchText?.length == 0) {
            val gainDataList = incomeNoteInteractor.getGainIncomeNoteInfoList()
            incomeNoteListAdapter?.setDataInfoList(gainDataList)
            incomeNoteListAdapter?.notifyDataSetChanged()
        } else {
            onStartSearch(searchText)
        }
    }

    fun onLossFilterClicked() {
        filterType = FILTER_TYPE_LOSS
        filterText = mContext.getString(R.string.Common_Loss)
        incomeNoteView.changeFilterText(filterText)
        if (searchText == null || searchText?.length == 0) {
            val lossDataList = incomeNoteInteractor.getLossIncomeNoteInfoList()
            incomeNoteListAdapter?.setDataInfoList(lossDataList)
            incomeNoteListAdapter?.notifyDataSetChanged()
        } else {
            onStartSearch(searchText)
        }
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
            incomeNoteListAdapter?.setDataInfoList(newDataList)
            incomeNoteListAdapter?.notifyDataSetChanged()
            incomeNoteView.showAddButton()
            incomeNoteView.scrollTopPosition(newDataList.size - 1)
            incomeNoteView.bindTotalGainData()
        } else {
            incomeNoteInteractor.insertIncomeNoteInfo(incomeNoteInfo)
            val newDataList = incomeNoteInteractor.getAllIncomeNoteInfoList()
            incomeNoteListAdapter?.setDataInfoList(newDataList)
            incomeNoteListAdapter?.notifyItemInserted(incomeNoteListAdapter?.itemCount!! - 1)
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
        val incomeNoteInfo = incomeNoteInteractor.getIncomeNoteInfo(position)
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
//        if (incomeNoteListAdapter?.isEditMode()!!) {
//            incomeNoteListAdapter?.setEditMode(false)
//            incomeNoteListAdapter?.notifyDataSetChanged()
//            incomeNoteView.showAddButton()
//        } else {
            Utils.runBackPressAppCloseEvent(mContext, activity)
//        }
    }

//    fun onMenuEditButtonClick() {
//        val allIncomeNoteList = incomeNoteInteractor.getAllIncomeNoteInfoList()
//        if (allIncomeNoteList.size > 0) {
//            incomeNoteListAdapter?.setEditMode(!incomeNoteListAdapter?.isEditMode()!!)
//            incomeNoteListAdapter?.notifyDataSetChanged()
//            if (incomeNoteListAdapter?.isEditMode()!!) {
//                incomeNoteView.hideAddButton()
//            } else {
//                incomeNoteView.showAddButton()
//            }
//        }
//    }

    fun getAllIncomeNoteList(): ArrayList<IncomeNoteInfo?> {
        return incomeNoteInteractor.getAllIncomeNoteInfoList()
    }

    //    fun onAdapterItemClick() {
//
//    }
    fun getEditMode(): Boolean {
        return editMode
    }

    fun onStartSearch(newText: String?) {
        //검색을 시작했을 때
        searchText = newText
        if (newText?.length!! > 0) {
            //필터 타입에 따라서 검색 내용을 보여준다.
            //검색바에서 필터링 한 List 선언
            val searchIncomeNoteList = incomeNoteInteractor.getSearchNoteList(newText)
            //검색바 및 전체, 이익, 손해 필터링 두개가 반영된 List 초기화
            val filteredIncomeNoteList = CopyOnWriteArrayList<IncomeNoteInfo>()
            filteredIncomeNoteList.addAll(searchIncomeNoteList)

            when (filterType) {
                FILTER_TYPE_ALL -> {
                    incomeNoteListAdapter?.setDataInfoList(searchIncomeNoteList)
                }
                FILTER_TYPE_GAIN -> {
                    filteredIncomeNoteList.forEach { incomeNoteInfo ->
                        val realGainLossesAmount = incomeNoteInfo?.realPainLossesAmount!!
                        val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                        if (realGainLossesAmountNum < 0) {
                            filteredIncomeNoteList.remove(incomeNoteInfo)
                        }
                    }
                    incomeNoteListAdapter?.setDataInfoList(filteredIncomeNoteList)
                }

                FILTER_TYPE_LOSS -> {
                    filteredIncomeNoteList.forEach { incomeNoteInfo ->
                        //실현손익
                        val realGainLossesAmount = incomeNoteInfo?.realPainLossesAmount!!
                        val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                        if (realGainLossesAmountNum > 0) {
                            filteredIncomeNoteList.remove(incomeNoteInfo)
                        }
                    }

                    incomeNoteListAdapter?.setDataInfoList(filteredIncomeNoteList)
                }
                else -> {
                    incomeNoteListAdapter?.setDataInfoList(searchIncomeNoteList)
                }
            }
        }
        //검색을 하다가 모두 지웠을 때 FilterType에 따라 보여줌.
        else {
            when (filterType) {
                FILTER_TYPE_ALL -> {
                    val allIncomeNoteList = incomeNoteInteractor.getAllIncomeNoteInfoList()
                    incomeNoteListAdapter?.setDataInfoList(allIncomeNoteList)
                }
                FILTER_TYPE_GAIN -> {
                    val gainIncomeNoteList = incomeNoteInteractor.getGainIncomeNoteInfoList()
                    incomeNoteListAdapter?.setDataInfoList(gainIncomeNoteList)
                }

                FILTER_TYPE_LOSS -> {
                    val lossIncomeNoteList = incomeNoteInteractor.getLossIncomeNoteInfoList()
                    incomeNoteListAdapter?.setDataInfoList(lossIncomeNoteList)
                }
                else -> {
                    val allIncomeNoteList = incomeNoteInteractor.getAllIncomeNoteInfoList()
                    incomeNoteListAdapter?.setDataInfoList(allIncomeNoteList)
                }
            }
        }
        incomeNoteListAdapter?.notifyDataSetChanged()
    }
    fun closeSwipeLayout() {
        incomeNoteListAdapter?.closeSwipeLayout()
    }
}
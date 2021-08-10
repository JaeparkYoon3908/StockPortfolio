package com.yjpapp.stockportfolio.function.incomenote

import android.app.Activity
import android.content.Context
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.localdb.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.model.IncomeNoteModel
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
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

    init {
        incomeNoteListAdapter = IncomeNoteListAdapter(this)
        incomeNoteView.setAdapter(incomeNoteListAdapter)
    }

    fun onAllFilterClicked() {
        filterType = FILTER_TYPE_ALL
        filterText = mContext.getString(R.string.Common_All)
        incomeNoteView.changeFilterText(filterText)
        if (searchText == null || searchText?.length == 0) {
            val allDataList = incomeNoteInteractor.getAllIncomeNoteInfoList() //            incomeNoteListAdapter?.setDataInfoList(allDataList)
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
            val gainDataList = incomeNoteInteractor.getGainIncomeNoteInfoList() //            incomeNoteListAdapter?.setDataInfoList(gainDataList)
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
            val lossDataList = incomeNoteInteractor.getLossIncomeNoteInfoList() //            incomeNoteListAdapter?.setDataInfoList(lossDataList)
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

    fun onInputDialogCompleteClicked(context: Context, incomeNoteList: IncomeNoteModel.IncomeNoteList?) { //id 설정
        incomeNoteList?.id = incomeNoteId

        if (editMode) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = incomeNoteInteractor.requestPutIncomeNote(context, incomeNoteList)
                result?.let {
                    if (it.isSuccessful) {
                        incomeNoteView.showToast(Toasty.normal(context, "수정완료"))
                        incomeNoteListAdapter?.refresh()
                    }
                }
            }

        } else {
            CoroutineScope(Dispatchers.Main).launch {
                val result = incomeNoteInteractor.requestPostIncomeNote(context, incomeNoteList)
                result?.let {
                    if (it.isSuccessful) {
                        incomeNoteView.showToast(Toasty.info(context, "추가완료"))
                        incomeNoteListAdapter?.refresh()
                    }
                }
            }
        }
    }

    fun onAdapterItemLongClick(isEditMode: Boolean) {
        if (isEditMode) {
            incomeNoteView.hideAddButton()
        } else {
            incomeNoteView.showAddButton()
        }
    }

    fun onEditButtonClick(incomeNoteList: IncomeNoteModel.IncomeNoteList?) {
        incomeNoteList?.let {
            editMode = true
            incomeNoteView.showAddButton()
            incomeNoteId = it.id //        val incomeNoteInfo = incomeNoteInteractor.getIncomeNoteInfo(position)
            incomeNoteView.showInputDialog(editMode, incomeNoteList)
        }
    }

    suspend fun onDeleteButtonClick(context: Context, id: Int) { //        incomeNoteInteractor.deleteIncomeNoteInfo(id)
        CoroutineScope(Dispatchers.IO).launch {
            val result = incomeNoteInteractor.requestDeleteIncomeNote(context, id)
            if (result!!.isSuccessful) {
                incomeNoteListAdapter?.refresh()
            }
        }
    }

    fun onBackPressedClick(activity: Activity) {
        Utils.runBackPressAppCloseEvent(mContext, activity)
    }

    fun getAllIncomeNoteList(): ArrayList<IncomeNoteInfo?> {
        return incomeNoteInteractor.getAllIncomeNoteInfoList()
    }

    fun getEditMode(): Boolean {
        return editMode
    }

    fun onStartSearch(newText: String?) { //검색을 시작했을 때
        searchText = newText
        if (newText?.length!! > 0) { //필터 타입에 따라서 검색 내용을 보여준다.
            //검색바에서 필터링 한 List 선언
            val searchIncomeNoteList = incomeNoteInteractor.getSearchNoteList(newText) //검색바 및 전체, 이익, 손해 필터링 두개가 반영된 List 초기화
            val filteredIncomeNoteList = CopyOnWriteArrayList<IncomeNoteInfo>()
            filteredIncomeNoteList.addAll(searchIncomeNoteList)

            when (filterType) {
                FILTER_TYPE_ALL -> {

                }
                FILTER_TYPE_GAIN -> {
                    filteredIncomeNoteList.forEach { incomeNoteInfo ->
                        val realGainLossesAmount = incomeNoteInfo?.realPainLossesAmount!!
                        val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                        if (realGainLossesAmountNum < 0) {
                            filteredIncomeNoteList.remove(incomeNoteInfo)
                        }
                    }
                }
                FILTER_TYPE_LOSS -> {
                    filteredIncomeNoteList.forEach { incomeNoteInfo -> //실현손익
                        val realGainLossesAmount = incomeNoteInfo?.realPainLossesAmount!!
                        val realGainLossesAmountNum = Utils.getNumDeletedComma(realGainLossesAmount).toDouble()
                        if (realGainLossesAmountNum > 0) {
                            filteredIncomeNoteList.remove(incomeNoteInfo)
                        }
                    }

                }
                else -> {

                }
            }
        } //검색을 하다가 모두 지웠을 때 FilterType에 따라 보여줌.
        else {
            when (filterType) {
                FILTER_TYPE_ALL -> {
                    val allIncomeNoteList =
                        incomeNoteInteractor.getAllIncomeNoteInfoList() //                    incomeNoteListAdapter?.setDataInfoList(allIncomeNoteList)
                }
                FILTER_TYPE_GAIN -> {
                    val gainIncomeNoteList =
                        incomeNoteInteractor.getGainIncomeNoteInfoList() //                    incomeNoteListAdapter?.setDataInfoList(gainIncomeNoteList)
                }

                FILTER_TYPE_LOSS -> {
                    val lossIncomeNoteList =
                        incomeNoteInteractor.getLossIncomeNoteInfoList() //                    incomeNoteListAdapter?.setDataInfoList(lossIncomeNoteList)
                }
                else -> {
                    val allIncomeNoteList =
                        incomeNoteInteractor.getAllIncomeNoteInfoList() //                    incomeNoteListAdapter?.setDataInfoList(allIncomeNoteList)
                }
            }
        }
        incomeNoteListAdapter?.notifyDataSetChanged()
    }

    fun closeSwipeLayout() {
        incomeNoteListAdapter?.closeSwipeLayout()
    }

    fun getIncomeNoteList(context: Context): Flow<PagingData<IncomeNoteModel.IncomeNoteList>> {
        return incomeNoteInteractor.getIncomeNoteListByPaging(context).cachedIn(CoroutineScope(Dispatchers.Main))
    }

    suspend fun submitData(pagingData: PagingData<IncomeNoteModel.IncomeNoteList>) {
        incomeNoteListAdapter?.submitData(pagingData)
    }
}
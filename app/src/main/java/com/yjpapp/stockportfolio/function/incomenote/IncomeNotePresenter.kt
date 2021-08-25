package com.yjpapp.stockportfolio.function.incomenote

import android.app.Activity
import android.content.Context
import androidx.paging.cachedIn
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
import com.yjpapp.stockportfolio.util.StockLog
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * IncomeNoteFragment의 Presenter
 *
 * @author Yoon Jae-park
 * @since 2020.12
 */

class IncomeNotePresenter(val mContext: Context, private val incomeNoteView: IncomeNoteView) {
    private val TAG = IncomeNotePresenter::class.java.simpleName
    private var editMode = false
    private var incomeNoteId = -1
    private val incomeNoteInteractor = IncomeNoteInteractor()
    private var incomeNoteListAdapter: IncomeNoteListAdapter? = null

    init {
        incomeNoteListAdapter = IncomeNoteListAdapter(this)
        incomeNoteView.setAdapter(incomeNoteListAdapter)
    }

    fun onAddButtonClicked() {
        editMode = false
        incomeNoteId = -1
        incomeNoteView.showInputDialog(editMode, null)
    }

    fun onInputDialogCompleteClicked(context: Context, respIncomeNoteList: RespIncomeNoteInfo.IncomeNoteList?) { //id 설정
        respIncomeNoteList?.id = incomeNoteId
        if (editMode) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = incomeNoteInteractor.requestPutIncomeNote(context, respIncomeNoteList)
                result?.let {
                    if (it.isSuccessful) {
                        incomeNoteView.showToast(Toasty.normal(context, "수정완료"))
                        incomeNoteListAdapter?.refresh()
                    }
                }
            }

        } else {
            CoroutineScope(Dispatchers.Main).launch {
                val result = incomeNoteInteractor.requestPostIncomeNote(context, respIncomeNoteList)
                result?.let {
                    if (it.isSuccessful) {
                        incomeNoteView.showToast(Toasty.info(context, "추가완료"))
                        incomeNoteListAdapter?.refresh()
                    }
                }
            }
        }
    }

    fun onEditButtonClick(respIncomeNoteList: RespIncomeNoteInfo.IncomeNoteList?) {
        respIncomeNoteList?.let {
            editMode = true
            incomeNoteView.showAddButton()
            incomeNoteId = it.id //        val incomeNoteInfo = incomeNoteInteractor.getIncomeNoteInfo(position)
            incomeNoteView.showInputDialog(editMode, respIncomeNoteList)
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

    fun getAllIncomeNoteList(): MutableList<RespIncomeNoteInfo.IncomeNoteList?> {
        incomeNoteListAdapter?.let {
            return it.snapshot().toMutableList()
        }
        return mutableListOf()
    }

    fun closeSwipeLayout() {
        incomeNoteListAdapter?.closeSwipeLayout()
    }

    suspend fun getIncomeNoteList(context: Context, startDate: String, endDate: String) {
        val incomeNoteList = incomeNoteInteractor.getIncomeNoteListByPaging(context, startDate, endDate).cachedIn(CoroutineScope(Dispatchers.Main))
        incomeNoteList.collectLatest {
            incomeNoteListAdapter?.submitData(it)
        }
    }

    /**
     * DatePickerDialog 영역
     */
    fun datePickerDialogConfirmClick(startDate: String, endDate: String) {
        StockLog.d(TAG, "startDate = $startDate")
        StockLog.d(TAG, "endDate = $endDate")
        CoroutineScope(Dispatchers.IO).launch {
            getIncomeNoteList(mContext, startDate, endDate)
        }
        incomeNoteView.initFilterDateText(startDate, endDate)
    }
}
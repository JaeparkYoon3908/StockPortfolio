package com.yjpapp.stockportfolio.function.incomenote

import android.app.Activity
import android.content.Context
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.localdb.sqlte.data.IncomeNoteInfo
import com.yjpapp.stockportfolio.model.IncomeNoteModel
import com.yjpapp.stockportfolio.util.StockLog
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.concurrent.CopyOnWriteArrayList

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

    fun closeSwipeLayout() {
        incomeNoteListAdapter?.closeSwipeLayout()
    }

    suspend fun getIncomeNoteList(context: Context) {
        val incomeNoteList = incomeNoteInteractor.getIncomeNoteListByPaging(context).cachedIn(CoroutineScope(Dispatchers.Main))
        incomeNoteList.collectLatest {
            incomeNoteListAdapter?.submitData(it)
        }
    }

    /**
     * DatePickerDialog 영역
     */
    fun datePickerDialogConfirmClick(startYYYYMM: String, endYYYYMM: String) {
        StockLog.d(TAG, "startYYYYMM = $startYYYYMM")
        StockLog.d(TAG, "endYYYYMM = $endYYYYMM")
    }
}
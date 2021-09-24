package com.yjpapp.stockportfolio.function.incomenote

import android.app.Activity
import android.content.Context
import androidx.paging.cachedIn
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteInfo
import com.yjpapp.stockportfolio.util.Utils
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import java.lang.StringBuilder

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
    var totalGainNumber = 0.0
    var totalGainPercent = 0.0
    var initStartYYYYMMDD = listOf<String>()
    var initEndYYYYMMDD = listOf<String>()

    init {
        incomeNoteListAdapter = IncomeNoteListAdapter(this)
        incomeNoteView.setAdapter(incomeNoteListAdapter)
    }

    fun onAddButtonClicked() {
        editMode = false
        incomeNoteId = -1
        incomeNoteView.showInputDialog(editMode, null)
    }

    fun onInputDialogCompleteClicked(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) { //id 설정
        reqIncomeNoteInfo.id = incomeNoteId
        if (editMode) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = incomeNoteInteractor.requestPutIncomeNote(context, reqIncomeNoteInfo)
                result?.let {
                    if (it.isSuccessful) {
                        incomeNoteView.showToast(Toasty.normal(context, "수정완료"))
                        incomeNoteListAdapter?.refresh()
                        requestTotalGain(context)
                    }
                }
            }
        } else {
            CoroutineScope(Dispatchers.Main).launch {
                val result = incomeNoteInteractor.requestPostIncomeNote(context, reqIncomeNoteInfo)
                result?.let {
                    if (it.isSuccessful) {
                        incomeNoteView.showToast(Toasty.info(context, "추가완료"))
                        incomeNoteListAdapter?.refresh()
                        requestTotalGain(context)
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

    fun onDeleteOkClick(context: Context, id: Int) { //        incomeNoteInteractor.deleteIncomeNoteInfo(id)
        CoroutineScope(Dispatchers.Main).launch {
            val result = incomeNoteInteractor.requestDeleteIncomeNote(context, id)
            result?.let {
                if (it.isSuccessful) {
                    incomeNoteView.showToast(Toasty.info(context, "삭제완료"))
                    incomeNoteListAdapter?.refresh()
                    requestTotalGain(context)
                }
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

    fun requestIncomeNoteList(mContext: Context, startDate: String, endDate: String) {
        val incomeNoteList = incomeNoteInteractor.getIncomeNoteListByPaging(mContext, startDate, endDate).cachedIn(CoroutineScope(Dispatchers.Main))
        CoroutineScope(Dispatchers.Main).launch {
            incomeNoteList.collectLatest {
                incomeNoteListAdapter?.submitData(it)
            }
        }
        initStartYYYYMMDD = startDate.split("-")
        initEndYYYYMMDD = endDate.split("-")
        incomeNoteView.initFilterDateText(startDate, endDate)
        requestTotalGain(mContext)
    }

    fun requestTotalGain(context: Context) {
        if (initStartYYYYMMDD.size == 3 && initEndYYYYMMDD.size == 3) {
            val startDate = StringBuilder()
            val endDate = StringBuilder()
            initStartYYYYMMDD.forEachIndexed { index, s ->
                startDate.append(s)
                if (index != 2) {
                    startDate.append("-")
                }
            }
            initEndYYYYMMDD.forEachIndexed { index, s ->
                endDate.append(s)
                if (index != 2) {
                    endDate.append("-")
                }
            }

            val params = hashMapOf<String, String>()
            params["startDate"] = startDate.toString()
            params["endDate"] = endDate.toString()
            CoroutineScope(Dispatchers.Main).launch {
                val result = incomeNoteInteractor.requestTotalGain(context, params)
                result?.let {
                    if (it.isSuccessful) {
                        it.body()?.let { data ->
                            incomeNoteView.bindTotalGainData(data.total_price, data.total_percent)
                        }
                    }
                }
            }
        }
    }
}
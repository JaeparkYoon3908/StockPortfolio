package com.yjpapp.stockportfolio.function.incomenote

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.extension.asEventFlow
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteListInfo
import com.yjpapp.stockportfolio.model.response.RespTotalGainIncomeNoteData
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.repository.IncomeNoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IncomeNoteViewModel @Inject constructor(
    private val incomeNoteRepository: IncomeNoteRepository
) : ViewModel() {
    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()
    var editMode = false
    var incomeNoteId = -1
    var page = 1
    private val pageSize = 20
    var initStartYYYYMMDD = listOf<String>()
    var initEndYYYYMMDD = listOf<String>()
    var hasNext = true

    fun requestGetIncomeNote(context: Context) {
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["size"] = pageSize.toString()
        if (initStartYYYYMMDD.isNotEmpty() && initEndYYYYMMDD.isNotEmpty()) {
            params["startDate"] = makeDateString(initStartYYYYMMDD)
            params["endDate"] = makeDateString(initEndYYYYMMDD)
        }
        viewModelScope.launch {
            try {
                val result = incomeNoteRepository.requestGetIncomeNote(context, params)
                if (result == null) {
                    ResponseAlertManger.showNetworkConnectErrorAlert(context)
                    return@launch
                }

                if (result.isSuccessful) {
                    result.body()?.let {
                        event(Event.FetchUIncomeNotes(it.income_note))
                        if (page * pageSize >= it.page_info.total_elements) {
                            hasNext = false
                        }
                    }
                }
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    fun requestTotalGain(context: Context) {
        val params = hashMapOf<String, String>()
        if (initStartYYYYMMDD.size == 3 && initEndYYYYMMDD.size == 3) {
            params["startDate"] = makeDateString(initStartYYYYMMDD)
            params["endDate"] = makeDateString(initEndYYYYMMDD)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = incomeNoteRepository.requestTotalGain(context, params)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (result.isSuccessful) {
                result.body()?.let {
                    event(Event.SendTotalGainData(it))
                }
            }
        }
    }

    fun requestDeleteIncomeNote(context: Context, id: Int, position: Int) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestDeleteIncomeNote(context, id)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (result.isSuccessful) {
                event(Event.IncomeNoteDeleteSuccess(position))
            }
        }
    }

    fun requestModifyIncomeNote(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestPutIncomeNote(context, reqIncomeNoteInfo)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (result.isSuccessful) {
                result.body()?.let { incomeNoteList ->
                    event(Event.IncomeNoteModifySuccess(incomeNoteList))
                }
            }
        }
    }

    fun requestAddIncomeNote(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestPostIncomeNote(context, reqIncomeNoteInfo)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (result.isSuccessful) {
                result.body()?.let { incomeNoteInfo ->
                    event(Event.IncomeNoteAddSuccess(incomeNoteInfo))
                }
            }
        }
    }

    fun makeDateString(dateList: List<String>): String {
        val result = StringBuilder()
        if (dateList.size == 3) {
            dateList.forEachIndexed { index, s ->
                result.append(s)
                if (index != 2) {
                    result.append("-")
                }
            }
        }
        return result.toString()
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    sealed class Event {
        data class SendTotalGainData(val data: RespTotalGainIncomeNoteData): Event()
        data class IncomeNoteDeleteSuccess(val position: Int): Event()
        data class IncomeNoteModifySuccess(val data: RespIncomeNoteListInfo.IncomeNoteInfo): Event()
        data class IncomeNoteAddSuccess(val data: RespIncomeNoteListInfo.IncomeNoteInfo): Event()
        data class FetchUIncomeNotes(val data: ArrayList<RespIncomeNoteListInfo.IncomeNoteInfo>): Event()
    }
}
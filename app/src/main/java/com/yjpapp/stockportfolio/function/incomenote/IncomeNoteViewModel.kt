package com.yjpapp.stockportfolio.function.incomenote

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.extension.asEventFlow
import com.yjpapp.stockportfolio.function.mystock.MyStockViewModel
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.model.request.ReqIncomeNoteInfo
import com.yjpapp.stockportfolio.model.response.RespIncomeNoteListInfo
import com.yjpapp.stockportfolio.model.response.RespTotalGainIncomeNoteData
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.repository.IncomeNoteRepository
import com.yjpapp.stockportfolio.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class IncomeNoteViewModel @Inject constructor(
    private val incomeNoteRepository: IncomeNoteRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<Event>(Event.InitUIState(""))
    val uiState: StateFlow<Event> get() = _uiState
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
                val result = incomeNoteRepository.requestGetIncomeNote(params)
                if (result == null) {
                    ResponseAlertManger.showNetworkConnectErrorAlert(context)
                    return@launch
                }
                if (!result.isSuccessful) {
                    event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                    return@launch
                }

                result.body()?.let {
                    event(Event.FetchUIncomeNotes(it.income_note))
                    if (page * pageSize >= it.page_info.total_elements) {
                        hasNext = false
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
            val result = incomeNoteRepository.requestTotalGain(params)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }

            result.body()?.let {
                event(Event.SendTotalGainData(it))
            }
        }
    }

    fun requestDeleteIncomeNote(context: Context, id: Int, position: Int) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestDeleteIncomeNote(id)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }

            event(Event.IncomeNoteDeleteSuccess(position))
        }
    }

    fun requestModifyIncomeNote(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestPutIncomeNote(reqIncomeNoteInfo)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }
            result.body()?.let { incomeNoteList ->
                event(Event.IncomeNoteModifySuccess(incomeNoteList))
            }
        }
    }

    fun requestAddIncomeNote(context: Context, reqIncomeNoteInfo: ReqIncomeNoteInfo) {
        viewModelScope.launch {
            val result = incomeNoteRepository.requestPostIncomeNote(reqIncomeNoteInfo)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }

            result.body()?.let { incomeNoteInfo ->
                event(Event.IncomeNoteAddSuccess(incomeNoteInfo))
            }
        }
    }

    fun makeDateString(dateList: List<String>): String {
        val result = StringBuffer()
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

    fun isShowDeleteCheck(): String {
        return incomeNoteRepository.isShowDeleteCheck()
    }

    fun runBackPressAppCloseEvent(mContext: Context, activity: Activity) {
        val isAllowAppClose = userRepository.isAllowAppClose()
        if (isAllowAppClose == StockConfig.TRUE) {
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        } else {
            Toasty.normal(mContext, mContext.getString(R.string.Common_BackButton_AppClose_Message)).show()
            userRepository.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.TRUE)
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                userRepository.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.FALSE)
            },3000)
        }
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _uiState.emit(event)
        }
    }

    sealed class Event {
        data class InitUIState(val msg: String = ""): Event()
        data class SendTotalGainData(val data: RespTotalGainIncomeNoteData): Event()
        data class IncomeNoteDeleteSuccess(val position: Int): Event()
        data class IncomeNoteModifySuccess(val data: RespIncomeNoteListInfo.IncomeNoteInfo): Event()
        data class IncomeNoteAddSuccess(val data: RespIncomeNoteListInfo.IncomeNoteInfo): Event()
        data class FetchUIncomeNotes(val data: ArrayList<RespIncomeNoteListInfo.IncomeNoteInfo>): Event()
        data class ResponseServerError(val msg: String): Event()
    }
}
package com.yjpapp.stockportfolio.function.incomenote

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.datasource.UserDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.model.request.ReqIncomeNoteInfo
import com.yjpapp.data.model.response.RespIncomeNoteListInfo
import com.yjpapp.data.model.response.RespTotalGainIncomeNoteData
import com.yjpapp.data.repository.IncomeNoteRepository
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.extension.EventFlow
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

@HiltViewModel
class IncomeNoteViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val incomeNoteRepository: IncomeNoteRepository,
    private val userRepository: UserDataSource
) : BaseViewModel() {
    private val _uiState = MutableEventFlow<Event>()
    val uiState: EventFlow<Event> get() = _uiState
    var editMode = false
    var incomeNoteId = -1
    var page = 1
    private val pageSize = 20
    var initStartYYYYMMDD = listOf<String>()
    private val _initStartYYYYMMDD get() = initStartYYYYMMDD
    var initEndYYYYMMDD = listOf<String>()
    private val _initEndYYYYMMDD get() = initEndYYYYMMDD
    var hasNext = true

    fun requestGetIncomeNote() {
        event(Event.StartAndStopLoadingAnimation(true))
        val params = HashMap<String, String>()
        params["page"] = page.toString()
        params["size"] = pageSize.toString()
        if (_initStartYYYYMMDD.isNotEmpty() && _initEndYYYYMMDD.isNotEmpty()) {
            params["startDate"] = makeDateString(_initStartYYYYMMDD)
            params["endDate"] = makeDateString(_initEndYYYYMMDD)
        }
        viewModelScope.launch {
            try {
                val result = incomeNoteRepository.getIncomeNote(params)
                if (result == null) {
                    event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                    return@launch
                }
                if (!result.isSuccessful) {
                    event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                    return@launch
                }

                result.data?.let {
                    event(Event.FetchUIncomeNotes(it.income_note))
                    hasNext = page * pageSize < it.page_info.total_elements
                }
                event(Event.StartAndStopLoadingAnimation(false))
            } catch (e: Exception) {
                e.stackTrace
            }
        }
    }

    fun requestTotalGain() {
        val params = hashMapOf<String, String>()
        if (_initStartYYYYMMDD.size == 3 && _initEndYYYYMMDD.size == 3) {
            params["startDate"] = makeDateString(_initStartYYYYMMDD)
            params["endDate"] = makeDateString(_initEndYYYYMMDD)
        }
        CoroutineScope(Dispatchers.Main).launch {
            val result = incomeNoteRepository.getTotalGain(params)
            if (result == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }

            result.data?.let {
                event(Event.SendTotalGainData(it))
            }
        }
    }

    fun requestDeleteIncomeNote(id: Int, position: Int) {
        viewModelScope.launch {
            val result = incomeNoteRepository.deleteIncomeNote(id)
            if (result == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }

            event(Event.IncomeNoteDeleteSuccess(position))
        }
    }

    fun requestModifyIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) {
        viewModelScope.launch {
            val result = incomeNoteRepository.modifyIncomeNote(reqIncomeNoteInfo)
            if (result == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }
            result.data?.let { incomeNoteList ->
                if (incomeNoteList.id == -1) {
                    event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Normal)))
                    return@launch
                }
                event(Event.IncomeNoteModifySuccess(incomeNoteList))
            }
        }
    }

    fun requestAddIncomeNote(reqIncomeNoteInfo: ReqIncomeNoteInfo) {
        viewModelScope.launch {
            val result = incomeNoteRepository.modifyIncomeNote(reqIncomeNoteInfo)
            if (result == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError("서버에 응답이 없습니다. 잠시 후 다시 시도해주세요."))
                return@launch
            }

            result.data?.let { incomeNoteInfo ->
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

    fun runBackPressAppCloseEvent(activity: Activity) {
        val isAllowAppClose = userRepository.isAllowAppClose()
        if (isAllowAppClose == StockConfig.TRUE) {
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        } else {
            Toasty.normal(context, context.getString(R.string.Common_BackButton_AppClose_Message)).show()
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
        data class SendTotalGainData(val data: RespTotalGainIncomeNoteData): Event()
        data class IncomeNoteDeleteSuccess(val position: Int): Event()
        data class IncomeNoteModifySuccess(val data: RespIncomeNoteListInfo.IncomeNoteInfo): Event()
        data class IncomeNoteAddSuccess(val data: RespIncomeNoteListInfo.IncomeNoteInfo): Event()
        data class FetchUIncomeNotes(val data: ArrayList<RespIncomeNoteListInfo.IncomeNoteInfo>): Event()
        data class ResponseServerError(val msg: String): Event()
        data class StartAndStopLoadingAnimation(val isAnimationStart: Boolean): Event()
    }
}
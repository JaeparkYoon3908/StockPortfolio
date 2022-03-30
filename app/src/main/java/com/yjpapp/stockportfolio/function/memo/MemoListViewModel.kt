package com.yjpapp.stockportfolio.function.memo

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
import com.yjpapp.stockportfolio.function.my.MyViewModel
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.repository.MemoRepository
import com.yjpapp.stockportfolio.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

/**
 * @author 윤재박
 * @since 2022.01.06
 */
@HiltViewModel
class MemoListViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val preferenceRepository: PreferenceRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<Event>(Event.InitUIState())
    val uiState: StateFlow<Event> get() = _uiState
    private var _allMemoListData = mutableListOf<MemoListEntity>()
    val allMemoListData get() = _allMemoListData
    fun getAllMemoInfoList() {
        _allMemoListData = memoRepository.getAllMemoInfoList()
        event(Event.SendToAllMemoListData(_allMemoListData))
    }

    fun requestDeleteMemoInfo() {
        val memoList = memoRepository.getAllMemoInfoList()
        try {
            for (position in memoList.indices) {
                if (memoList[position].deleteChecked == "true") {
                    memoRepository.deleteMemoInfoList(memoList[position])
                    val updateMemoList = memoRepository.getAllMemoInfoList()
                    event(Event.SendToAllMemoListData(updateMemoList))
                }
            }
            val updateMemoList = memoRepository.getAllMemoInfoList()
            event(Event.MemoListDataDeleteSuccess(updateMemoList))
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun requestUpdateDeleteCheck(position: Int, deleteCheck: Boolean) {
        val id = memoRepository.getAllMemoInfoList()[position].id
        memoRepository.updateDeleteCheck(id, deleteCheck.toString())
    }

    fun runBackPressAppCloseEvent(mContext: Context, activity: Activity) {
        val isAllowAppClose = preferenceRepository.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)?: StockConfig.FALSE
        if (isAllowAppClose == StockConfig.TRUE) {
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        } else {
            Toasty.normal(mContext, mContext.getString(R.string.Common_BackButton_AppClose_Message)).show()
            preferenceRepository.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.TRUE)
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                preferenceRepository.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.FALSE)
            },3000)
        }
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _uiState.emit(event)
        }
    }

    fun isMemoVibration(): Boolean {
        return memoRepository.getIsMemoVibration()
    }

    sealed class Event {
        data class InitUIState(val msg: String = ""): Event()
        data class SendToAllMemoListData(val data: MutableList<MemoListEntity>): Event()
        data class MemoListDataDeleteSuccess(val data: MutableList<MemoListEntity>): Event()
    }
}
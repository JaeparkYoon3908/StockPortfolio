package com.yjpapp.stockportfolio.function.memo

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.datasource.MemoDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.extension.EventFlow
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

/**
 * @author 윤재박
 * @since 2022.01.06
 */
@HiltViewModel
class MemoListViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val memoRepository: MemoDataSource,
    private val preferenceRepository: PreferenceDataSource
) : ViewModel() {
    private val _uiState = MutableEventFlow<Event>()
    val uiState: EventFlow<Event> get() = _uiState
    private var _allMemoListData = mutableListOf<MemoListEntity>()
    val allMemoListData get() = _allMemoListData
    fun getAllMemoInfoList() {
        _allMemoListData = memoRepository.getAllMemoInfoList()
        event(Event.RefreshMemoListData(_allMemoListData))
    }

    fun requestDeleteMemoInfo() {
        val memoList = memoRepository.getAllMemoInfoList()
        try {
            for (position in memoList.indices) {
                if (memoList[position].deleteChecked == "true") {
                    memoRepository.deleteMemoInfoList(memoList[position])
                    val updateMemoList = memoRepository.getAllMemoInfoList()
                    event(Event.RefreshMemoListData(updateMemoList))
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

    fun runBackPressAppCloseEvent(activity: Activity) {
        val isAllowAppClose = preferenceRepository.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)?: StockConfig.FALSE
        if (isAllowAppClose == StockConfig.TRUE) {
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        } else {
            Toasty.normal(context, context.getString(R.string.Common_BackButton_AppClose_Message)).show()
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
        data class RefreshMemoListData(val data: MutableList<MemoListEntity>): Event()
        data class MemoListDataDeleteSuccess(val data: MutableList<MemoListEntity>): Event()
    }
}
package com.yjpapp.stockportfolio.function.memo

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.extension.asEventFlow
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.repository.MemoRepository
import com.yjpapp.stockportfolio.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val repository: MemoRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _eventFlow = MutableEventFlow<Event>()
    val eventFlow = _eventFlow.asEventFlow()
    private var _allMemoListData = mutableListOf<MemoListEntity>()
    val allMemoListData get() = _allMemoListData
    fun getAllMemoInfoList() {
        _allMemoListData = repository.getAllMemoInfoList()
        event(Event.SendToAllMemoListData(_allMemoListData))
    }

    fun requestDeleteMemoInfo() {
        val memoList = repository.getAllMemoInfoList()
        try {
            for (position in memoList.indices) {
                if (memoList[position].deleteChecked == "true") {
                    repository.deleteMemoInfoList(memoList[position])
                    val updateMemoList = repository.getAllMemoInfoList()
                    event(Event.SendToAllMemoListData(updateMemoList))
                }
            }
            val updateMemoList = repository.getAllMemoInfoList()
            event(Event.MemoListDataDeleteSuccess(updateMemoList))
        } catch (e: Exception) {
            e.stackTrace
        }
    }

    fun requestUpdateDeleteCheck(position: Int, deleteCheck: Boolean) {
        val id = repository.getAllMemoInfoList()[position].id
        repository.updateDeleteCheck(id, deleteCheck.toString())
    }

    fun runBackPressAppCloseEvent(mContext: Context, activity: Activity) {
        val isAllowAppClose = userRepository.isAllowAppClose()
        if (isAllowAppClose == StockConfig.TRUE) {
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        } else {
            Toasty.normal(mContext, mContext.getString(R.string.Common_BackButton_AppClose_Message)).show()
            userRepository.setAllowAppClose(StockConfig.TRUE)
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                userRepository.setAllowAppClose(StockConfig.FALSE)
            },3000)
        }
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _eventFlow.emit(event)
        }
    }

    fun isMemoVibration(): Boolean {
        return repository.getIsMemoVibration()
    }

    sealed class Event {
        data class SendToAllMemoListData(val data: MutableList<MemoListEntity>): Event()
        data class MemoListDataDeleteSuccess(val data: MutableList<MemoListEntity>): Event()
    }
}
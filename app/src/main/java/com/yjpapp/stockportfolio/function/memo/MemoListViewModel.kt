package com.yjpapp.stockportfolio.function.memo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.extension.asEventFlow
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.repository.MemoListRepository
import kotlinx.coroutines.launch

/**
 * @author 윤재박
 * @since 2022.01.06
 */
class MemoListViewModel(
    val repository: MemoListRepository
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
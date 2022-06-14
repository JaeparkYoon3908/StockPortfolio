package com.yjpapp.stockportfolio.function.memo.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.datasource.MemoDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import com.yjpapp.data.repository.MyRepository
import com.yjpapp.data.repository.UserRepository
import com.yjpapp.stockportfolio.extension.EventFlow
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * since 2022.01.05
 * Presenter -> ViewModel 형식으로 변경
 */
@HiltViewModel
class MemoReadWriteViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val memoRepository: MemoDataSource,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableEventFlow<Event>()
    val uiState: EventFlow<Event> get() = _uiState
    var mode: String? = null
    var memoListPosition = 0
    var id = 0
    var savedTitle: String = ""
    var savedContent: String = ""

    fun requestAddMemoData(date: String, title: String, content: String){
        val memoInfo = MemoListEntity(0, date, title, content, "false")
        memoRepository.requestInsertMemoData(memoInfo)
    }

    fun requestUpdateMemoData(date: String, title: String, content: String){
        val memoInfo = MemoListEntity(id, date, title, content, "false")
        memoRepository.requestUpdateMemoData(memoInfo)
    }

    fun requestDeleteMemoData() {
        event(Event.SendDeleteResult(memoRepository.requestDeleteMomoData(id)))
    }

    fun requestGetPreference(prefKey: String): String {
        return userRepository.getPreference(prefKey)
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _uiState.emit(event)
        }
    }

    sealed class Event {
        data class SendDeleteResult(val isSuccess: Boolean): Event()
    }
}
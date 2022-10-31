package com.yjpapp.stockportfolio.function.memo.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.datasource.MemoDataSource
import com.yjpapp.data.localdb.room.memo.MemoListEntity
import com.yjpapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val _uiState = MutableSharedFlow<Event>(
        replay = 0, //replay = 0 : 새로운 구독자에게 이전 이벤트를 전달하지 않음
        extraBufferCapacity = 1, //추가 버퍼를 생성하여 emit 한 데이터가 버퍼에 유지 되도록함
        onBufferOverflow = BufferOverflow.DROP_OLDEST //버퍼가 가득찼을 시 오래된 데이터 제거
    )
    val uiState = _uiState.asSharedFlow() //convert read only
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
package com.yjpapp.stockportfolio.function.memo.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.extension.EventFlow
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.localdb.room.memo.MemoListEntity
import com.yjpapp.stockportfolio.repository.MemoRepository
import com.yjpapp.stockportfolio.repository.PreferenceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * since 2022.01.05
 * Presenter -> ViewModel 형식으로 변경
 */
@HiltViewModel
class MemoReadWriteViewModel @Inject constructor(
    private val memoRepository: MemoRepository,
    private val preferenceRepository: PreferenceRepository
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
        memoRepository.insertMemoData(memoInfo)
    }

    fun requestUpdateMemoData(date: String, title: String, content: String){
        val memoInfo = MemoListEntity(id, date, title, content, "false")
        memoRepository.updateMemoData(memoInfo)
    }

    fun requestDeleteMemoData() {
        event(Event.SendDeleteResult(memoRepository.deleteMomoData(id)))
    }

    fun requestSetPreference(prefKey: String, value: String) {
        preferenceRepository.setPreference(prefKey, value)
    }

    fun requestGetPreference(prefKey: String): String {
        return preferenceRepository.getPreference(prefKey)?: ""
    }

    fun requestIsExistPreference(prefKey: String): Boolean {
        return preferenceRepository.isExists(prefKey)
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
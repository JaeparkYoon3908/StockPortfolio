package com.yjpapp.stockportfolio.test

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.test.model.LatestNewsUiState
import com.yjpapp.stockportfolio.test.model.UserMessage
import com.yjpapp.stockportfolio.util.Event
import com.yjpapp.stockportfolio.util.NetworkUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(LatestNewsUiState(isLoading = true))
    val uiState: StateFlow<LatestNewsUiState> get() = _uiState

    private val _liveData = MutableLiveData<Int>()
    val liveData: LiveData<Int> get() = _liveData

    private val _eventLiveData = MutableLiveData<Event<String>>()
    val eventLiveData: LiveData<Event<String>> get() = _eventLiveData

    var activityDataSendText: String = ""

    fun refreshNews(context: Context, message: String) {
        viewModelScope.launch {
            // If there isn't internet connection, show a new message on the screen.
            if (!NetworkUtils.isInternetAvailable(context)) {
                _uiState.update { currentUiState ->
                    val messages = currentUiState.userMessages + UserMessage(
                        id = UUID.randomUUID().mostSignificantBits,
                        message = "No Internet connection"
                    )
                    currentUiState.copy(userMessages = messages)
                }
                return@launch
            }
            // Do something else.
            _uiState.update { currentUiState ->
                val messages = currentUiState.userMessages + UserMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    message = message
                )
                currentUiState.copy(userMessages = messages)
            }
        }
    }

    fun userMessageShown(messageId: Long) {
        _uiState.update { currentUiState ->
            val messages = currentUiState.userMessages.filterNot { it.id == messageId }
            currentUiState.copy(userMessages = messages)
        }
    }

    fun refreshNewLiveData(message: String) {
        _liveData.value = 0
    }

    fun testViewModelScope() {
        viewModelScope.launch {
            repeat(5) {
                _liveData.value = it
                delay(1000)
            }
//            for (i in 0 until 5) {
//                _liveData.value = i
//                delay(1000)
//            }
        }
    }
}
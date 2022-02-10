package com.yjpapp.stockportfolio.test

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.test.model.LatestNewsUiState
import com.yjpapp.stockportfolio.test.model.UserMessage
import com.yjpapp.stockportfolio.util.NetworkUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.*

class TestViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LatestNewsUiState(isLoading = true))
    val uiState: StateFlow<LatestNewsUiState> = _uiState

    private val _liveData = MutableLiveData<String>()
    val liveData: LiveData<String> = _liveData

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

            _uiState.update { currentUiState ->
                val messages = currentUiState.userMessages + UserMessage(
                    id = UUID.randomUUID().mostSignificantBits,
                    message = message
                )
                currentUiState.copy(userMessages = messages)
            }
            // Do something else.
        }
    }

    fun userMessageShown(messageId: Long) {
        _uiState.update { currentUiState ->
            val messages = currentUiState.userMessages.filterNot { it.id == messageId }
            currentUiState.copy(userMessages = messages)
        }
    }

    fun refreshNewLiveData(message: String) {
        _liveData.value = message
    }

    fun userMessageShownByLiveData() {

    }
}
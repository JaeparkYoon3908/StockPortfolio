package com.yjpapp.stockportfolio.function.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navercorp.nid.NaverIdLoginSDK
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.extension.EventFlow
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespLoginUserInfo
import com.yjpapp.stockportfolio.model.response.RespNaverDeleteUserInfo
import com.yjpapp.stockportfolio.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author 윤재박
 * @since 2021.07
 */

@HiltViewModel
class LoginViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository

) : ViewModel() {
    /**
     * Common
     */
    private val TAG = LoginViewModel::class.java.simpleName
    private val _uiState = MutableEventFlow<Event>()
    val uiState: EventFlow<Event> get() = _uiState

    /**
     * API
     */
    fun requestLogin(reqSnsLogin: ReqSNSLogin) {
        viewModelScope.launch {
            val result = userRepository.postUserInfo(reqSnsLogin)
            if (result == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                return@launch
            }
            if (result.body() == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Normal)))
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Normal)))
                return@launch
            }
            event(Event.ResponseLoginResultData(result.body()!!))
        }
    }

    fun requestDeleteNaverUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val naverAccessToken = userRepository.getNaverAccessToken()
            val params = HashMap<String, String>()
            params["client_id"] = StockConfig.NAVER_SIGN_CLIENT_ID
            params["client_secret"] = StockConfig.NAVER_SIGN_CLIENT_SECRET
            params["access_token"] = naverAccessToken
            params["grant_type"] = "delete"
            params["service_provider"] = "NAVER"

            val result = userRepository.deleteNaverUserInfo(params)
            if (result == null) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Network_Connect_Exception)))
                return@launch
            }
            if (!result.isSuccessful) {
                event(Event.ResponseServerError(context.getString(R.string.Error_Msg_Normal)))
                return@launch
            }
            event(Event.ResponseDeleteNaverUserInfo(result.body()))
        }
    }

    fun requestSetPreference(prefKey: String, value: String?) {
        userRepository.setPreference(prefKey, value)
    }

    fun requestGetPreference(prefKey: String): String {
        return userRepository.getPreference(prefKey)
    }

    fun requestIsExistPreference(prefKey: String): Boolean {
        return userRepository.isExistPreference(prefKey)
    }

    fun requestNaverLogout() {
        NaverIdLoginSDK.logout()
    }

    private fun event(event: Event) {
        viewModelScope.launch {
            _uiState.emit(event)
        }
    }

    sealed class Event {
        data class ResponseServerError(val msg: String): Event()
        data class ResponseDeleteNaverUserInfo(val respNaverDeleteUserInfo: RespNaverDeleteUserInfo?): Event()
        data class ResponseLoginResultData(val respLoginUserInfo: RespLoginUserInfo): Event()
    }
}
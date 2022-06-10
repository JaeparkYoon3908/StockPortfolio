package com.yjpapp.stockportfolio.function.login

import android.content.Context
import androidx.lifecycle.viewModelScope
import com.navercorp.nid.NaverIdLoginSDK
import com.yjpapp.data.datasource.UserDataSource
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqSNSLogin
import com.yjpapp.data.model.response.RespLoginUserInfo
import com.yjpapp.data.model.response.RespNaverDeleteUserInfo
import com.yjpapp.data.repository.UserRepository
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.extension.EventFlow
import com.yjpapp.stockportfolio.extension.MutableEventFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
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
) : BaseViewModel() {
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
            val result = userRepository.addUserInfo(reqSnsLogin)
            if (result is ResponseResult.DataError) {
                event(Event.ResponseServerError(result.resultMessage))
                return@launch
            }
            result.data?.let { data ->
                event(Event.ResponseLoginResultData(data))
            }
        }
    }

    fun requestDeleteNaverUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val naverAccessToken = userRepository.getNaverAccessToken()
            val params = HashMap<String, String>()
            params["client_id"] = BuildConfig.NAVER_SIGN_CLIENT_ID
            params["client_secret"] = BuildConfig.NAVER_SIGN_CLIENT_SECRET
            params["access_token"] = naverAccessToken
            params["grant_type"] = "delete"
            params["service_provider"] = "NAVER"
            val result = userRepository.deleteNaverUserInfo(params)
            if (result is ResponseResult.DataError) {
                event(Event.ResponseServerError(result.resultMessage))
                return@launch
            }
            result.data?.let { data ->
                event(Event.ResponseDeleteNaverUserInfo(data))
            }
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
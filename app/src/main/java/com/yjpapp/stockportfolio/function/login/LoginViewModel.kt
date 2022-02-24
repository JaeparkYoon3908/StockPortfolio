package com.yjpapp.stockportfolio.function.login

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.navercorp.nid.NaverIdLoginSDK
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespGetNaverUserInfo
import com.yjpapp.stockportfolio.model.response.RespLoginUserInfo
import com.yjpapp.stockportfolio.model.response.RespNaverDeleteUserInfo
import com.yjpapp.stockportfolio.network.ServerRespCode
import com.yjpapp.stockportfolio.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
    private val userRepository: UserRepository

) : ViewModel() {
    /**
     * Common
     */
    private val TAG = LoginViewModel::class.java.simpleName
    private val _serverError = MutableStateFlow(0)
    val serverError: StateFlow<Int> get() = _serverError

    /**
     * API
     */
    private val _loginResultData = MutableStateFlow(RespLoginUserInfo())
    val loginResultData: StateFlow<RespLoginUserInfo> get() = _loginResultData
    fun requestLogin(reqSnsLogin: ReqSNSLogin) {
        viewModelScope.launch {
            val result = userRepository.postUserInfo(reqSnsLogin)
            if (result == null) {
                _serverError.emit(ServerRespCode.NetworkNotConnected)
                return@launch
            }
            if (result.body() == null) {
                return@launch
            }
            if (!result.isSuccessful) {
                return@launch
            }
            _loginResultData.emit(result.body()!!)
        }
    }

    val respDeleteNaverUserInfo = MutableLiveData<RespNaverDeleteUserInfo>()
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
                _serverError.emit(ServerRespCode.NetworkNotConnected)
                return@launch
            }
            if (result.isSuccessful) {
                respDeleteNaverUserInfo.postValue(result.body())
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
}
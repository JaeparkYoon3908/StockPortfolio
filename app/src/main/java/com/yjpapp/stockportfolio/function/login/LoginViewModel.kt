package com.yjpapp.stockportfolio.function.login

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespGetNaverUserInfo
import com.yjpapp.stockportfolio.model.response.RespLoginUserInfo
import com.yjpapp.stockportfolio.model.response.RespNaverDeleteUserInfo
import com.yjpapp.stockportfolio.network.ServerRespCode
import com.yjpapp.stockportfolio.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    val serverError = MutableLiveData<Int>()
    /**
     * API
     */
    val loginResultData = MutableLiveData<RespLoginUserInfo>()
    fun requestLogin(context: Context, reqSnsLogin: ReqSNSLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.postUserInfo(context, reqSnsLogin)
            if (result == null) {
                serverError.postValue(ServerRespCode.NetworkNotConnected)
                return@launch
            }
            if (result.isSuccessful && result.body() != null) {
                when (result.body()!!.status) {

                }
                loginResultData.postValue(result.body())
            }
        }
    }

    val respGetNaverUserInfo = MutableLiveData<RespGetNaverUserInfo>()
    fun requestGetNaverUserInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.getNaverUserInfo(context)
            if (result == null) {
                serverError.postValue(ServerRespCode.NetworkNotConnected)
                return@launch
            }
            if (result.isSuccessful) {
                respGetNaverUserInfo.postValue(result.body())
            } else {

            }
        }
    }

    fun requestRetryNaverUserLogin(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val naverAccessToken = userRepository.getNaverAccessToken()
            val params = HashMap<String, String>()
            params["client_id"] = StockConfig.NAVER_SIGN_CLIENT_ID
            params["response_type"] = "code"
            params["redirect_uri"] = ""
            params["state"] = ""
            params["auth_type"] = "reprompt"

            val result = userRepository.retryNaverUserLogin(context, params)
            if (result == null) {
                serverError.postValue(ServerRespCode.NetworkNotConnected)
                return@launch
            }
            if (result.isSuccessful) {

            } else {

            }
        }
    }

    val respDeleteNaverUserInfo = MutableLiveData<RespNaverDeleteUserInfo>()
    fun requestDeleteNaverUserInfo(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val naverAccessToken = userRepository.getNaverAccessToken()
            val params = HashMap<String, String>()
            params["client_id"] = StockConfig.NAVER_SIGN_CLIENT_ID
            params["client_secret"] = StockConfig.NAVER_SIGN_CLIENT_SECRET
            params["access_token"] = naverAccessToken
            params["grant_type"] = "delete"
            params["service_provider"] = "NAVER"

            val result = userRepository.deleteNaverUserInfo(context, params)
            if (result == null) {
                serverError.postValue(ServerRespCode.NetworkNotConnected)
                return@launch
            }
            if (result.isSuccessful) {
                respDeleteNaverUserInfo.postValue(result.body())
            }
        }
    }

    fun requestSetPreference(prefKey: String, value: String) {
        userRepository.setPreference(prefKey, value)
    }

    fun requestGetPreference(prefKey: String): String {
        return userRepository.getPreference(prefKey)
    }

    fun requestIsExistPreference(prefKey: String): Boolean {
        return userRepository.isExistPreference(prefKey)
    }

}
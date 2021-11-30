package com.yjpapp.stockportfolio.function.login

import android.app.Application
import android.content.Context
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.dialog.CommonOneBtnDialog
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespLoginUserInfo
import com.yjpapp.stockportfolio.model.response.RespGetNaverUserInfo
import com.yjpapp.stockportfolio.model.response.RespNaverDeleteUserInfo
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.network.ServerRespCode
import com.yjpapp.stockportfolio.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author 윤재박
 * @since 2021.07
 */

class LoginViewModel(
    application: Application,
    private val preferenceController: PreferenceController,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {
    /**
     * Common
     */
    private val TAG = LoginViewModel::class.java.simpleName
    val serverError = MutableLiveData<Int>()
    /**
     * API
     */
    val loginResultData = MutableLiveData<RespLoginUserInfo>()
    fun requestLogin(reqSnsLogin: ReqSNSLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.postUserInfo(getApplication(), reqSnsLogin)
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
    fun requestGetNaverUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.getNaverUserInfo(getApplication())
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

    fun requestRetryNaverUserLogin() {
        viewModelScope.launch(Dispatchers.IO) {
            val naverAccessToken = preferenceController.getPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN)?: ""
            val params = HashMap<String, String>()
            params["client_id"] = StockConfig.NAVER_SIGN_CLIENT_ID
            params["response_type"] = "code"
            params["redirect_uri"] = ""
            params["state"] = ""
            params["auth_type"] = "reprompt"

            val result = userRepository.retryNaverUserLogin(getApplication(), params)
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
    fun requestDeleteNaverUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val naverAccessToken = preferenceController.getPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN)?: ""
            val params = HashMap<String, String>()
            params["client_id"] = StockConfig.NAVER_SIGN_CLIENT_ID
            params["client_secret"] = StockConfig.NAVER_SIGN_CLIENT_SECRET
            params["access_token"] = naverAccessToken
            params["grant_type"] = "delete"
            params["service_provider"] = "NAVER"

            val result = userRepository.deleteNaverUserInfo(getApplication(), params)
            if (result == null) {
                serverError.postValue(ServerRespCode.NetworkNotConnected)
                return@launch
            }
            if (result.isSuccessful) {
                respDeleteNaverUserInfo.postValue(result.body())
            }
        }
    }
}
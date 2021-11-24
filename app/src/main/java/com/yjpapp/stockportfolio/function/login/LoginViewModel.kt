package com.yjpapp.stockportfolio.function.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespLoginUserInfo
import com.yjpapp.stockportfolio.model.response.RespGetNaverUserInfo
import com.yjpapp.stockportfolio.model.response.RespNaverDeleteUserInfo
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
    val isNetworkConnectException = MutableLiveData<Boolean>()

    /**
     * API
     */
    val loginResultData = MutableLiveData<RespLoginUserInfo>()
    fun requestLogin(reqSnsLogin: ReqSNSLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.postUserInfo(getApplication(), reqSnsLogin)
            if (result == null) {
                isNetworkConnectException.postValue(true)
                return@launch
            }
            if (result.isSuccessful) {
                loginResultData.postValue(result.body())
            }
        }
    }

    val respGetNaverUserInfo = MutableLiveData<RespGetNaverUserInfo>()
    fun requestGetNaverUserInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.getNaverUserInfo(getApplication())
            if (result == null) {
                isNetworkConnectException.value = true
                return@launch
            }
            if (result.isSuccessful) {
                respGetNaverUserInfo.postValue(result.body())
            }
        }
    }
}
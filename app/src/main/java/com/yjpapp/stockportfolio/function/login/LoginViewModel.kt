package com.yjpapp.stockportfolio.function.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespLoginUserInfo
import com.yjpapp.stockportfolio.model.response.RespNaverUserInfo
import com.yjpapp.stockportfolio.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author 윤재박
 * @since 2021.07
 */

class LoginViewModel(application: Application, private val userRepository: UserRepository): AndroidViewModel(application) {
    /**
     * Common
     */
    private val TAG = LoginViewModel::class.java.simpleName
    /**
     * API
     */
    val loginResultData = MutableLiveData<RespLoginUserInfo>()
    fun requestLogin(reqSnsLogin: ReqSNSLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = userRepository.postUserInfo(getApplication(), reqSnsLogin)
            result?.let {
                if (it.isSuccessful) {
                    loginResultData.postValue(it.body())
                }
            }
        }
    }

    val respNaverUserInfo = MutableLiveData<RespNaverUserInfo>()
    fun requestNaverUserInfo() {
        viewModelScope.launch(Dispatchers.IO){
            val result = userRepository.getNaverUserInfo(getApplication())
                result?.let {
                if (it.isSuccessful) {
                    respNaverUserInfo.postValue(it.body())
                }
            }
        }
    }
}
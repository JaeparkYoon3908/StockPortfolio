package com.yjpapp.stockportfolio.function.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.model.response.RespLoginUserInfo
import com.yjpapp.stockportfolio.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author 윤재박
 * @since 2021.07
 */

class LoginViewModel(application: Application, private val userRepository: UserRepository): AndroidViewModel(application) {
    val loginResultData = MutableLiveData<RespLoginUserInfo>()

    fun requestSNSLogin(reqSnsLogin: ReqSNSLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            val authorization = PreferenceController.getInstance(getApplication()).getPreference(PrefKey.KEY_USER_TOKEN)?: ""
            val result = userRepository.postUserInfo(getApplication(), reqSnsLogin, authorization)
            result?.let {
                if (it.isSuccessful) {
                    loginResultData.postValue(it.body())
                }
            }
        }
    }

    fun requestNaverUserInfo(params: HashMap<String, String>) {
        viewModelScope.launch(Dispatchers.IO){
            val authorization = PreferenceController.getInstance(getApplication()).getPreference(PrefKey.KEY_USER_TOKEN)?: ""
            val result = userRepository.getNaverUserInfo(getApplication(), params, authorization)
            result?.let {
                if (it.isSuccessful) {
//                    loginResultData.postValue()
                }
            }
        }
    }
}
package com.yjpapp.stockportfolio.function.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.model.SNSLoginRequest
import com.yjpapp.stockportfolio.model.SNSLoginResult
import com.yjpapp.stockportfolio.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author 윤재박
 * @since 2021.07
 */

class LoginViewModel(application: Application, private val userRepository: UserRepository): AndroidViewModel(application) {
    val loginResultData = MutableLiveData<SNSLoginResult>()
    val autoLoginStatus = MutableLiveData<Int>()
    val userInfo = MutableLiveData<SNSLoginResult>()

    fun postUserInfo(context: Context, snsLoginRequest: SNSLoginRequest) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = userRepository.postUserInfo(context, snsLoginRequest)
            result?.let {
                if (it.isSuccessful) {
                    userInfo.value = it.body()
                }
            }
        }
    }

    fun getUserInfo(context: Context, params: HashMap<String, String>) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = userRepository.getUserInfo(context, params)
            result?.let {
                if (it.isSuccessful) {
                    userInfo.postValue(it.body())
                }
            }
        }
    }

}
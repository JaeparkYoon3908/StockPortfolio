package com.yjpapp.stockportfolio.ui.login

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yjpapp.stockportfolio.model.SNSLoginRequest
import com.yjpapp.stockportfolio.model.SNSLoginResult
import com.yjpapp.stockportfolio.network.RetrofitClient
import com.yjpapp.stockportfolio.util.StockLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * @author 윤재박
 * @since 2021.07
 */
class LoginViewModel(application: Application): AndroidViewModel(application) {
    val loginResultData = MutableLiveData<SNSLoginResult>()

    fun requestUserReg(context: Context, snsLoginRequest: SNSLoginRequest){
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.getService(context)?.let {
                it.requestRegistUser(snsLoginRequest).let { response ->
                    if (response.isSuccessful) {
                        loginResultData.value = response.body()
                    } else {
                        StockLog.d("YJP", "response.body() = " + response.errorBody())
                    }
                }
            }
        }
    }
}
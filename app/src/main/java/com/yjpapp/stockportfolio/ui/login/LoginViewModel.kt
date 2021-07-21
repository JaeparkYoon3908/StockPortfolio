package com.yjpapp.stockportfolio.ui.login

import android.app.Activity
import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.facebook.CallbackManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.yjpapp.stockportfolio.constance.AppConfig
import com.yjpapp.stockportfolio.model.SNSLoginRequest
import com.yjpapp.stockportfolio.model.SNSLoginResult
import com.yjpapp.stockportfolio.network.RetrofitClient
import com.yjpapp.stockportfolio.repository.SNSLoginRepository
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
    val autoLoginStatus = MutableLiveData<Int>()

    fun requestUserReg(context: Context, snsLoginRequest: SNSLoginRequest) {
        loginResultData.value = SNSLoginRepository.getInstance().requestUserReg(context, snsLoginRequest)
    }

}
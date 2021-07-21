package com.yjpapp.stockportfolio.repository

import android.content.Context
import android.content.Intent
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.SNSLoginRequest
import com.yjpapp.stockportfolio.model.SNSLoginResult
import com.yjpapp.stockportfolio.network.RetrofitClient
import com.yjpapp.stockportfolio.ui.login.LoginActivity
import com.yjpapp.stockportfolio.util.StockLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SNSLoginRepository {

    companion object {
        @Volatile
        private var instance: SNSLoginRepository? = null

        @JvmStatic
        fun getInstance(): SNSLoginRepository = instance ?: synchronized(this) {
            instance ?: SNSLoginRepository().also {
                instance = it
            }
        }
    }

    fun requestUserReg(context: Context, snsLoginRequest: SNSLoginRequest): SNSLoginResult? = runBlocking {
        RetrofitClient.getService(context)?.let {
            it.requestRegiUser(snsLoginRequest).let { response ->
                if (response.isSuccessful) {
                    return@runBlocking response.body()
                } else {
                    StockLog.d("YJP", "response.body() = " + response.errorBody())
                    return@runBlocking response.body()
                }
            }
        }
    }

    fun requestAutoLogin(context: Context, index: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            RetrofitClient.getService(context)?.let {
                return@let it.requestUserInfo(index).let { response ->
                    if (response.isSuccessful) {

                    } else {
                        StockLog.d("YJP", "response.body() = " + response.errorBody())
                    }
                }
            }
        }
    }

    fun logout(context: Context) {
        //프리퍼런스 reset
        PreferenceController.getInstance(context).setPreference(PrefKey.KEY_AUTO_LOGIN, false)
        PreferenceController.getInstance(context).setPreference(PrefKey.KEY_USER_INDEX, "")
        PreferenceController.getInstance(context).setPreference(PrefKey.KEY_USER_TOKEN, "")

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }
}
package com.yjpapp.stockportfolio.repository

import android.content.Context
import android.content.Intent
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.SNSLoginRequest
import com.yjpapp.stockportfolio.network.RetrofitClient
import com.yjpapp.stockportfolio.function.login.LoginActivity

class UserRepository {

    suspend fun postUserInfo(context: Context, snsLoginRequest: SNSLoginRequest) =
        RetrofitClient.getService(context)?.requestRegiUser(snsLoginRequest)

    suspend fun getUserInfo(context: Context, params: HashMap<String, String>) =
        RetrofitClient.getService(context)?.requestUserInfo(params)

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
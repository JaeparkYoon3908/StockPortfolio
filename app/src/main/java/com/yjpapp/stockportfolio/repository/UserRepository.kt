package com.yjpapp.stockportfolio.repository

import android.content.Context
import android.content.Intent
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.network.RetrofitClient
import com.yjpapp.stockportfolio.function.login.LoginActivity

class UserRepository(
    private val preferenceController: PreferenceController
) {

    suspend fun postUserInfo(context: Context, reqSnsLogin: ReqSNSLogin) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestRegUser(reqSnsLogin)

    suspend fun getUserInfo(context: Context, params: HashMap<String, String>) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestUserInfo(params)

    suspend fun getNaverUserInfo(context: Context) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.NAVER_OPEN_API)?.requestGetNaverUserInfo()

    suspend fun deleteNaverUserInfo(context: Context, params: HashMap<String, String>) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.NAVER_NID)?.requestDeleteNaverUserInfo(params)

    suspend fun retryNaverUserLogin(context: Context, params: HashMap<String, String>) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.NAVER_NID)?.requestRetryNaverUserLogin(params)

    fun logout(context: Context) {
        //프리퍼런스 reset
        preferenceController.setPreference(PrefKey.KEY_AUTO_LOGIN, false)
        preferenceController.setPreference(PrefKey.KEY_USER_INDEX, "")
        preferenceController.setPreference(PrefKey.KEY_USER_TOKEN, "")

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    suspend fun deleteUserInfo(context: Context) =
        RetrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestDeleteUserInfo()

    fun getLoginType(): String {
        return preferenceController.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)?: ""
    }
}
package com.yjpapp.stockportfolio.repository

import android.content.Context
import android.content.Intent
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.function.login.LoginActivity
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.network.RetrofitClient

class UserRepository(
    private val preferenceRepository: PreferenceRepository,
    private val retrofitClient: RetrofitClient
) {

    suspend fun postUserInfo(reqSnsLogin: ReqSNSLogin) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestRegUser(reqSnsLogin)

    suspend fun getUserInfo(params: HashMap<String, String>) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestUserInfo(params)

    suspend fun getNaverUserInfo() =
        retrofitClient.getService(RetrofitClient.BaseServerURL.NAVER_OPEN_API)?.requestGetNaverUserInfo()

    suspend fun deleteNaverUserInfo(params: HashMap<String, String>) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.NAVER_NID)?.requestDeleteNaverUserInfo(params)

    suspend fun retryNaverUserLogin(params: HashMap<String, String>) =
        retrofitClient.getService(RetrofitClient.BaseServerURL.NAVER_NID)?.requestRetryNaverUserLogin(params)

    fun logout(context: Context) {
        //프리퍼런스 reset
        preferenceRepository.setPreference(PrefKey.KEY_AUTO_LOGIN, false)
        preferenceRepository.setPreference(PrefKey.KEY_USER_INDEX, "")
        preferenceRepository.setPreference(PrefKey.KEY_USER_TOKEN, "")

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    suspend fun deleteUserInfo() =
        retrofitClient.getService(RetrofitClient.BaseServerURL.RaspberryPi)?.requestDeleteUserInfo()

    fun getLoginType(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)?: ""
    }

    fun getNaverAccessToken(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN)?: ""
    }

    fun getUserName(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_USER_NAME)?: ""
    }

    fun getUserEmail(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_USER_EMAIL)?: ""
    }

    fun isAllowAppClose(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)?: StockConfig.FALSE
    }

    fun setPreference(prefKey: String, value: String?) {
        preferenceRepository.setPreference(prefKey, value)
    }

    fun setPreference(prefKey: String, value: Int) {
        preferenceRepository.setPreference(prefKey, value)
    }

    fun getPreference(prefKey: String): String {
        return preferenceRepository.getPreference(prefKey)?: ""
    }

    fun isExistPreference(prefKey: String): Boolean {
        return preferenceRepository.isExists(prefKey)
    }
}

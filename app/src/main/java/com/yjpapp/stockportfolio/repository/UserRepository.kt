package com.yjpapp.stockportfolio.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.function.login.LoginActivity
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.network.RetrofitClient
import es.dmoral.toasty.Toasty
import kotlin.system.exitProcess

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

    fun runBackPressAppCloseEvent(
        mContext: Context,
        activity: Activity,
    ){
        val isAllowAppClose = preferenceRepository.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)
        if(isAllowAppClose == StockConfig.TRUE){
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        }else{
            Toasty.normal(mContext, mContext.getString(R.string.Common_BackButton_AppClose_Message)).show()
            preferenceRepository.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.TRUE)
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                preferenceRepository.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.FALSE)
            },3000)
        }
    }

    fun isAllowAppClose(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)?: StockConfig.FALSE
    }

    fun setPreference(prefKey: String, value: String) {
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

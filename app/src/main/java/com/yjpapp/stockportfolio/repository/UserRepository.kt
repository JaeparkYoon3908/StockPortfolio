package com.yjpapp.stockportfolio.repository

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.model.request.ReqSNSLogin
import com.yjpapp.stockportfolio.network.RetrofitClient
import com.yjpapp.stockportfolio.function.login.LoginActivity
import es.dmoral.toasty.Toasty
import kotlin.system.exitProcess

class UserRepository(
    private val preferenceController: PreferenceController,
    private val retrofitClient: RetrofitClient
) {

    suspend fun postUserInfo(context: Context, reqSnsLogin: ReqSNSLogin) =
        retrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestRegUser(reqSnsLogin)

    suspend fun getUserInfo(context: Context, params: HashMap<String, String>) =
        retrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestUserInfo(params)

    suspend fun getNaverUserInfo(context: Context) =
        retrofitClient.getService(context, RetrofitClient.BaseServerURL.NAVER_OPEN_API)?.requestGetNaverUserInfo()

    suspend fun deleteNaverUserInfo(context: Context, params: HashMap<String, String>) =
        retrofitClient.getService(context, RetrofitClient.BaseServerURL.NAVER_NID)?.requestDeleteNaverUserInfo(params)

    suspend fun retryNaverUserLogin(context: Context, params: HashMap<String, String>) =
        retrofitClient.getService(context, RetrofitClient.BaseServerURL.NAVER_NID)?.requestRetryNaverUserLogin(params)

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
        retrofitClient.getService(context, RetrofitClient.BaseServerURL.MY)?.requestDeleteUserInfo()

    fun getLoginType(): String {
        return preferenceController.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)?: ""
    }

    fun getNaverAccessToken(): String {
        return preferenceController.getPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN)?: ""
    }

    fun getUserName(): String {
        return preferenceController.getPreference(PrefKey.KEY_USER_NAME)?: ""
    }

    fun getUserEmail(): String {
        return preferenceController.getPreference(PrefKey.KEY_USER_EMAIL)?: ""
    }

    fun runBackPressAppCloseEvent(
        mContext: Context,
        activity: Activity,
    ){
        val isAllowAppClose = preferenceController.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)
        if(isAllowAppClose == StockConfig.TRUE){
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        }else{
            Toasty.normal(mContext, mContext.getString(R.string.Common_BackButton_AppClose_Message)).show()
            preferenceController.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.TRUE)
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                preferenceController.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.FALSE)
            },3000)
        }
    }

    fun isAllowAppClose(): String {
        return preferenceController.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)?: StockConfig.FALSE
    }

    fun setAllowAppClose(isAllowClose: String) {
        preferenceController.setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, isAllowClose)
    }
}

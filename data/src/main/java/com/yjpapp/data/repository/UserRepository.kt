package com.yjpapp.data.repository

import com.yjpapp.data.StockConfig
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.datasource.UserDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.model.request.ReqSNSLogin
import com.yjpapp.data.network.service.RaspberryPiService
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val preferenceDataSource: PreferenceDataSource
): BaseRepository() {

    suspend fun addUserInfo(reqSnsLogin: ReqSNSLogin) =
        userDataSource.requestPostUserInfo(reqSnsLogin)

    suspend fun getUserInfo(params: HashMap<String, String>) =
        userDataSource.requestGetUserInfo(params)

    suspend fun getNaverUserInfo() =
        userDataSource.requestGetNaverUserInfo()

    suspend fun deleteNaverUserInfo(params: HashMap<String, String>) =
        userDataSource.requestDeleteNaverUserInfo(params)

    suspend fun retryNaverUserLogin(params: HashMap<String, String>) =
        userDataSource.requestRetryNaverUserLogin(params)

    fun logout() {
        //프리퍼런스 reset
        preferenceDataSource.setPreference(PrefKey.KEY_AUTO_LOGIN, false)
        preferenceDataSource.setPreference(PrefKey.KEY_USER_INDEX, "")
        preferenceDataSource.setPreference(PrefKey.KEY_USER_TOKEN, "")
    }

    suspend fun deleteUserInfo() = userDataSource.requestDeleteUserInfo()

    fun getLoginType(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)?: ""
    }

    fun getNaverAccessToken(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN)?: ""
    }

    fun getUserName(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_USER_NAME)?: ""
    }

    fun getUserEmail(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_USER_EMAIL)?: ""
    }

    fun isAllowAppClose(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)?: StockConfig.FALSE
    }

    fun setPreference(prefKey: String, value: String?) {
        preferenceDataSource.setPreference(prefKey, value)
    }

    fun setPreference(prefKey: String, value: Int) {
        preferenceDataSource.setPreference(prefKey, value)
    }

    fun getPreference(prefKey: String): String {
        return preferenceDataSource.getPreference(prefKey)?: ""
    }

    fun isExistPreference(prefKey: String): Boolean {
        return preferenceDataSource.isExists(prefKey)
    }
}
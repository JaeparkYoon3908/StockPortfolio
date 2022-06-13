package com.yjpapp.data.repository

import com.yjpapp.data.StockConfig
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.datasource.UserDataSource
import com.yjpapp.data.localdb.preference.PrefKey
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqSNSLogin
import com.yjpapp.data.model.response.RespGetNaverUserInfo
import com.yjpapp.data.model.response.RespLoginUserInfo
import com.yjpapp.data.model.response.RespNaverDeleteUserInfo
import com.yjpapp.data.model.response.RespStatusInfo
import javax.inject.Inject

interface UserRepository {
    suspend fun addUserInfo(reqSnsLogin: ReqSNSLogin): ResponseResult<RespLoginUserInfo>
    suspend fun getUserInfo(params: HashMap<String, String>): ResponseResult<RespLoginUserInfo>
    suspend fun getNaverUserInfo(): ResponseResult<RespGetNaverUserInfo>
    suspend fun deleteNaverUserInfo(params: HashMap<String, String>): ResponseResult<RespNaverDeleteUserInfo>
    suspend fun retryNaverUserLogin(params: HashMap<String, String>): ResponseResult<RespNaverDeleteUserInfo>
    fun logout()
    suspend fun deleteUserInfo(): ResponseResult<RespStatusInfo>
    fun getLoginType(): String
    fun getNaverAccessToken(): String
    fun getUserName(): String
    fun getUserEmail(): String
    fun isAllowAppClose(): String
    fun setPreference(prefKey: String, value: String?)
    fun setPreference(prefKey: String, value: Int)
    fun getPreference(prefKey: String): String
    fun isExistPreference(prefKey: String): Boolean
}
class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource,
    private val preferenceDataSource: PreferenceDataSource
): UserRepository {

    override suspend fun addUserInfo(reqSnsLogin: ReqSNSLogin) =
        userDataSource.requestPostUserInfo(reqSnsLogin)

    override suspend fun getUserInfo(params: HashMap<String, String>) =
        userDataSource.requestGetUserInfo(params)

    override suspend fun getNaverUserInfo() =
        userDataSource.requestGetNaverUserInfo()

    override suspend fun deleteNaverUserInfo(params: HashMap<String, String>) =
        userDataSource.requestDeleteNaverUserInfo(params)

    override suspend fun retryNaverUserLogin(params: HashMap<String, String>) =
        userDataSource.requestRetryNaverUserLogin(params)

    override fun logout() {
        //프리퍼런스 reset
        preferenceDataSource.setPreference(PrefKey.KEY_AUTO_LOGIN, false)
        preferenceDataSource.setPreference(PrefKey.KEY_USER_INDEX, "")
        preferenceDataSource.setPreference(PrefKey.KEY_USER_TOKEN, "")
    }

    override suspend fun deleteUserInfo() = userDataSource.requestDeleteUserInfo()

    override fun getLoginType(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)?: ""
    }

    override fun getNaverAccessToken(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_NAVER_ACCESS_TOKEN)?: ""
    }

    override fun getUserName(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_USER_NAME)?: ""
    }

    override fun getUserEmail(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_USER_EMAIL)?: ""
    }

    override fun isAllowAppClose(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE)?: StockConfig.FALSE
    }

    override fun setPreference(prefKey: String, value: String?) {
        preferenceDataSource.setPreference(prefKey, value)
    }

    override fun setPreference(prefKey: String, value: Int) {
        preferenceDataSource.setPreference(prefKey, value)
    }

    override fun getPreference(prefKey: String): String {
        return preferenceDataSource.getPreference(prefKey)?: ""
    }

    override fun isExistPreference(prefKey: String): Boolean {
        return preferenceDataSource.isExists(prefKey)
    }
}
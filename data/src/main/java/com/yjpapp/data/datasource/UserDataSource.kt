package com.yjpapp.data.datasource

import com.yjpapp.data.model.request.ReqSNSLogin
import com.yjpapp.data.network.service.NaverNidService
import com.yjpapp.data.network.service.NaverOpenService
import com.yjpapp.data.network.service.RaspberryPiService

class UserDataSource(
    private val raspberryPiService: RaspberryPiService?,
    private val naverOpenService: NaverOpenService?,
    private val naverNIDService: NaverNidService?
) {
    suspend fun requestPostUserInfo(reqSnsLogin: ReqSNSLogin) =
        raspberryPiService?.requestRegUser(reqSnsLogin)

    suspend fun requestGetUserInfo(params: HashMap<String, String>) =
        raspberryPiService?.requestUserInfo(params)

    suspend fun requestGetNaverUserInfo() =
        naverNIDService?.requestGetNaverUserInfo()

    suspend fun requestDeleteNaverUserInfo(params: HashMap<String, String>) =
        naverOpenService?.requestDeleteNaverUserInfo(params)

    suspend fun requestRetryNaverUserLogin(params: HashMap<String, String>) =
        naverOpenService?.requestRetryNaverUserLogin(params)

    suspend fun requestDeleteUserInfo() =
        raspberryPiService?.requestDeleteUserInfo()
}

package com.yjpapp.data.datasource

import com.yjpapp.data.APICall
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.request.ReqSNSLogin
import com.yjpapp.data.model.response.*
import com.yjpapp.data.network.ServerRespCode
import com.yjpapp.data.network.ServerRespMessage
import com.yjpapp.data.network.service.NaverNidService
import com.yjpapp.data.network.service.NaverOpenService
import com.yjpapp.data.network.service.RaspberryPiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserDataSource(
    private val raspberryPiService: RaspberryPiService,
) {
    suspend fun requestPostUserInfo(reqSnsLogin: ReqSNSLogin): ResponseResult<RespLoginUserInfo> =
        APICall.handleApi { raspberryPiService.requestRegUser(reqSnsLogin) }

    suspend fun requestGetUserInfo(params: HashMap<String, String>): ResponseResult<RespLoginUserInfo> =
        APICall.handleApi { raspberryPiService.requestUserInfo(params) }

    suspend fun requestDeleteUserInfo(): ResponseResult<RespStatusInfo> =
        APICall.handleApi { raspberryPiService.requestDeleteUserInfo() }
}

package com.yjpapp.stockportfolio.data.datasource

import com.yjpapp.stockportfolio.data.APICall
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.stockportfolio.data.model.request.ReqSNSLogin
import com.yjpapp.data.model.response.*
import com.yjpapp.stockportfolio.data.network.service.RaspberryPiService

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

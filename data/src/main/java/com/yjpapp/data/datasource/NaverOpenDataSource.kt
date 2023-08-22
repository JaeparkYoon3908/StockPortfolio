package com.yjpapp.data.datasource

import com.yjpapp.data.APICall
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.response.RespNaverDeleteUserInfo
import com.yjpapp.data.network.service.NaverOpenService
import javax.inject.Inject

class NaverOpenDataSource @Inject constructor(
    private val naverOpenService: NaverOpenService
){
    suspend fun requestDeleteNaverUserInfo(params: HashMap<String, String>): ResponseResult<RespNaverDeleteUserInfo> =
        APICall.handleApi { naverOpenService.requestDeleteNaverUserInfo(params) }

    suspend fun requestRetryNaverUserLogin(params: HashMap<String, String>): ResponseResult<RespNaverDeleteUserInfo> =
        APICall.handleApi { naverOpenService.requestRetryNaverUserLogin(params) }
}
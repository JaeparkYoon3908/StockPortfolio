package com.yjpapp.data.datasource

import com.yjpapp.data.APICall
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.model.response.RespGetNaverUserInfo
import com.yjpapp.data.network.service.NaverNidService
import javax.inject.Inject

class NaverNIDDataSource @Inject constructor(
    private val naverNIDService: NaverNidService
) {
    suspend fun requestGetNaverUserInfo(): ResponseResult<RespGetNaverUserInfo> =
        APICall.handleApi { naverNIDService.requestGetNaverUserInfo() }
}
package com.yjpapp.stockportfolio.data.datasource

import com.yjpapp.stockportfolio.data.APICall
import com.yjpapp.stockportfolio.data.model.ResponseResult
import com.yjpapp.stockportfolio.data.model.response.RespGetNaverUserInfo
import com.yjpapp.stockportfolio.data.network.service.NaverNidService
import javax.inject.Inject

class NaverNIDDataSource @Inject constructor(
    private val naverNIDService: NaverNidService
) {
    suspend fun requestGetNaverUserInfo(): ResponseResult<RespGetNaverUserInfo> =
        APICall.handleApi { naverNIDService.requestGetNaverUserInfo() }
}
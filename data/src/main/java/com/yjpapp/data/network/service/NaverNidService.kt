package com.yjpapp.data.network.service

import com.yjpapp.data.model.response.RespGetNaverUserInfo
import retrofit2.Response
import retrofit2.http.GET

interface NaverNidService {
    @GET("/v1/nid/me")
    suspend fun requestGetNaverUserInfo(): Response<RespGetNaverUserInfo>
}
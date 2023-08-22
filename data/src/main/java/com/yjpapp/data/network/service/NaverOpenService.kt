package com.yjpapp.data.network.service

import com.yjpapp.data.model.response.RespNaverDeleteUserInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NaverOpenService {
    @GET("/oauth2.0/token")
    suspend fun requestDeleteNaverUserInfo(@QueryMap params: HashMap<String, String>): Response<RespNaverDeleteUserInfo>

    @GET("/oauth2.0/authorize")
    suspend fun requestRetryNaverUserLogin(@QueryMap params: HashMap<String, String>): Response<RespNaverDeleteUserInfo>
}
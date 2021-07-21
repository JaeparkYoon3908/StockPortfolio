package com.yjpapp.stockportfolio.network

import com.yjpapp.stockportfolio.model.SNSLoginRequest
import com.yjpapp.stockportfolio.model.SNSLoginResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RetrofitService {
//    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/user/regist_user")
    suspend fun requestRegiUser(@Body snsLoginRequest: SNSLoginRequest): Response<SNSLoginResult>

    @GET("user/user_info")
    suspend fun requestUserInfo(index: Int): Response<SNSLoginResult>

}
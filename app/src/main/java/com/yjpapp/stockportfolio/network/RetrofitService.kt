package com.yjpapp.stockportfolio.network

import com.yjpapp.stockportfolio.localdb.sqlte.data.MyStockInfo
import com.yjpapp.stockportfolio.model.SNSLoginRequest
import com.yjpapp.stockportfolio.model.SNSLoginResult
import retrofit2.Response
import retrofit2.http.*

interface RetrofitService {
//    @Headers("Content-Type:application/json;charset=UTF-8")
    @POST("/user/regist_user")
    suspend fun requestRegiUser(@Body snsLoginRequest: SNSLoginRequest): Response<SNSLoginResult>

//    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("/user/user_info")
    suspend fun requestUserInfo(@QueryMap params: HashMap<String, String>): Response<SNSLoginResult>

    @GET("/user/my_stock")
    suspend fun requestMyStockList(@QueryMap params: HashMap<String, String>): Response<MyStockInfo>


}
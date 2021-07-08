package com.yjpapp.stockportfolio.network

import com.google.gson.JsonObject
import com.yjpapp.stockportfolio.model.TestModel
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers

interface RetrofitService {
    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("/user/regist_user")
    suspend fun getMyJsonObject(@Body testModel: TestModel): Deferred<Response<JsonObject>>

}
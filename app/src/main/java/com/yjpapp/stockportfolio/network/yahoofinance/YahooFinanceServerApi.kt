package com.yjpapp.stockportfolio.network.yahoofinance

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface YahooFinanceServerApi {
    @Headers("Content-Type:application/json;charset=UTF-8")
    @GET("/stock/v2/get-profile")
    fun getMyJsonObject(@HeaderMap headerMap:Map<String, String>, @QueryMap queryMap:Map<String, String>): Call<JsonObject?>?
}
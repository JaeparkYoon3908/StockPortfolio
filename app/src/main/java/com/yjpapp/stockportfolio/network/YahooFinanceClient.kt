package com.yjpapp.stockportfolio.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class YahooFinanceClient {
    companion object {
        val ourInstance = YahooFinanceClient()
        const val url =
            "https://apidojo-yahoo-finance-v1.p.rapidapi.com"
//        fun getInstance():RetrofitKtClient{
//            return ourInstance
//        }
    }

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
        .build()
    val service: YahooFinanceServerApi = retrofit.create(YahooFinanceServerApi::class.java)
}
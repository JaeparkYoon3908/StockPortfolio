package com.yjpapp.stockportfolio.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        val ourInstance = RetrofitClient()
        const val baseUrl = "http://112.147.50.202/"
        fun getInstance(): RetrofitClient {
            return ourInstance
        }
    }

    private val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
        .build()
    val service: RetrofitService = retrofit.create(RetrofitService::class.java)
}
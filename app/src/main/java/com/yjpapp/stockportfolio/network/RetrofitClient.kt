package com.yjpapp.stockportfolio.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.ConnectException
import java.util.concurrent.TimeUnit

class RetrofitClient {
    companion object {
        val ourInstance = RetrofitClient()
        const val BASE_URL = "http://112.147.50.202/"
        const val CONNECT_TIMEOUT_OUT_MINUTE:Long = 3
        const val READ_TIMEOUT_OUT_MINUTE:Long = 3

        fun getInstance(): RetrofitClient {
            return ourInstance
        }

        fun getService(context: Context): RetrofitService? {
            if (isInternetAvailable(context)) {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY
                val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .retryOnConnectionFailure(true)
                    .connectTimeout(CONNECT_TIMEOUT_OUT_MINUTE, TimeUnit.MINUTES)
                    .readTimeout(READ_TIMEOUT_OUT_MINUTE, TimeUnit.MINUTES)
                    .build()
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()) // 파싱등록
                    .build()

                return retrofit.create(RetrofitService::class.java)
            } else {
                return null
            }
        }

        private fun isInternetAvailable(context: Context): Boolean {
            var result = false
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val networkCapabilities = connectivityManager.activeNetwork ?: return false
                val actNw =
                    connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
                result = when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    else -> false
                }
            } else {
                connectivityManager.run {
                    connectivityManager.activeNetworkInfo?.run {
                        result = when (type) {
                            ConnectivityManager.TYPE_WIFI -> true
                            ConnectivityManager.TYPE_MOBILE -> true
                            ConnectivityManager.TYPE_ETHERNET -> true
                            else -> false
                        }

                    }
                }
            }

            return result
        }
    }
    class NetworkNotConnectException : IOException() {
        override val message: String
            get() = "NetworkNotConnectException"
    }
}
package com.yjpapp.stockportfolio.network

import android.content.Context
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.util.NetworkUtils
import com.yjpapp.stockportfolio.util.StockLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitClient {
    enum class BaseServerURL(val url: String) {
        MY("http://112.147.50.241"),
        NAVER_OPEN_API("https://openapi.naver.com"),
        NAVER_NID("https://nid.naver.com")
    }
    private val TAG = RetrofitClient::class.java.simpleName
    const val CONNECT_TIMEOUT_OUT_MINUTE: Long = 3
    const val READ_TIMEOUT_OUT_MINUTE: Long = 3

    fun getService(context: Context, baseServerURL: BaseServerURL): RetrofitService? {
        if (NetworkUtils.isInternetAvailable(context)) {
            val interceptor: Interceptor = object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val builder =
                        when (baseServerURL) {
                            BaseServerURL.MY -> {
                                val authorization = PreferenceController.getInstance(context).getPreference(PrefKey.KEY_USER_TOKEN)?: ""
                                getClientBuilderWithToken(context, chain, authorization)
                            }
                            BaseServerURL.NAVER_OPEN_API -> {
                                val authorization = PreferenceController.getInstance(context).getPreference(PrefKey.KEY_NAVER_USER_TOKEN)?: ""
                                getNaverClientBuilderWithToken(chain, authorization)
                            }
                            BaseServerURL.NAVER_NID -> {
                                getNaverClientBuilderNID(chain)
                            }
                        }
                    val response: Response = chain.proceed(builder.build())
                    response.peekBody(Int.MAX_VALUE.toLong())
                    try {
                        if (!response.isSuccessful) {
                            StockLog.d(TAG, "getClient() error code: " + response.code)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    return response
                }
            }

            val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }

            val okHttpBuilder = OkHttpClient.Builder().apply {
                addInterceptor(interceptor)
                if (BuildConfig.DEBUG) {
                    addInterceptor(httpLoggingInterceptor)
                }
                retryOnConnectionFailure(true)
                connectTimeout(CONNECT_TIMEOUT_OUT_MINUTE, TimeUnit.MINUTES)
                readTimeout(READ_TIMEOUT_OUT_MINUTE, TimeUnit.MINUTES)
            }
            val client = okHttpBuilder.build()

            val retrofit = Retrofit.Builder().apply {
                baseUrl(baseServerURL.url)
                client(client)
                addConverterFactory(GsonConverterFactory.create()) // 파싱등록
            }.build()

            return retrofit.create(RetrofitService::class.java)
        } else {
            return null
        }
    }

    private fun getClientBuilderWithToken(context: Context, chain: Interceptor.Chain, authorization: String): Request.Builder {
        val preferenceController = PreferenceController.getInstance(context)
        val builder = chain.request().newBuilder().addHeader("Content-Type", "application/json")
        return builder
            .addHeader("Authorization", authorization)
//            .addHeader("Content-Type", "application/json")
            .addHeader("user-index", preferenceController.getPreference(PrefKey.KEY_USER_INDEX)?: "")
//            .addHeader("user-index", "10005")
    }

    private fun getNaverClientBuilderWithToken(chain: Interceptor.Chain, authorization: String): Request.Builder {
        return chain.request().newBuilder()
            .removeHeader("user-index")
            .addHeader("Authorization", authorization)
    }

    private fun getNaverClientBuilderNID(chain: Interceptor.Chain): Request.Builder {
        return chain.request().newBuilder()
            .removeHeader("Authorization")
            .removeHeader("user-index")
    }
}
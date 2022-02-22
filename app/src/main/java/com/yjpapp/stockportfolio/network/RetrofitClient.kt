package com.yjpapp.stockportfolio.network

import android.content.Context
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.repository.PreferenceRepository
import com.yjpapp.stockportfolio.util.NetworkUtils
import com.yjpapp.stockportfolio.util.StockLog
import okhttp3.*
import okhttp3.internal.http2.ConnectionShutdownException
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class RetrofitClient(
    private val context: Context,
    private val preferenceRepository: PreferenceRepository
) {
    enum class BaseServerURL(val url: String) {
        RaspberryPi("http://112.147.50.241"),
        NAVER_OPEN_API("https://openapi.naver.com"),
        NAVER_NID("https://nid.naver.com")
    }

    private val TAG = RetrofitClient::class.java.simpleName
    private val CONNECT_TIMEOUT_OUT_MINUTE: Long = 3
    private val READ_TIMEOUT_OUT_MINUTE: Long = 3

    fun getService(baseServerURL: BaseServerURL): RetrofitService? {
        if (!NetworkUtils.isInternetAvailable(context)) {
            return null
        }
        val interceptor: Interceptor = CustomInterceptor(context, baseServerURL, preferenceRepository)
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
            if (BuildConfig.DEBUG) {
                addInterceptor(httpLoggingInterceptor)
            }
            retryOnConnectionFailure(true)
            connectTimeout(CONNECT_TIMEOUT_OUT_MINUTE, TimeUnit.MINUTES)
            readTimeout(READ_TIMEOUT_OUT_MINUTE, TimeUnit.MINUTES)
        }.build()

        val retrofit = Retrofit.Builder().apply {
            baseUrl(baseServerURL.url)
            client(client)
            addConverterFactory(GsonConverterFactory.create()) // 파싱등록
        }.build()

        return retrofit.create(RetrofitService::class.java)
    }

    class CustomInterceptor(
        private val context: Context,
        private val baseServerURL: BaseServerURL,
        private val preferenceRepository: PreferenceRepository
    ) : Interceptor {
        private val TAG = CustomInterceptor::class.java.simpleName
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder =
                when (baseServerURL) {
                    BaseServerURL.RaspberryPi -> {
                        val authorization = preferenceRepository
                            .getPreference(PrefKey.KEY_USER_TOKEN) ?: ""
                        getClientBuilderWithToken(context, chain, authorization)
                    }
                    BaseServerURL.NAVER_OPEN_API -> {
                        val authorization = preferenceRepository
                            .getPreference(PrefKey.KEY_NAVER_USER_TOKEN) ?: ""
                        getNaverClientBuilderWithToken(chain, authorization)
                    }
                    BaseServerURL.NAVER_NID -> {
                        getNaverClientBuilderNID(chain)
                    }
                }

            val request = chain.request()
            try {
                val response: Response = chain.proceed(builder.build()).apply {
                    peekBody(Int.MAX_VALUE.toLong())
                }
                if (!response.isSuccessful) {
                    StockLog.d(TAG, "getClient() error code: " + response.code)
                }
                return response
            } catch (e: Exception) {
                e.printStackTrace()
                var msg = ""
                when (e) {
                    is SocketTimeoutException -> {
                        msg = "Timeout - Please check your internet connection"
                    }
                    is UnknownHostException -> {
                        msg = "Unable to make a connection. Please check your internet"
                    }
                    is ConnectionShutdownException -> {
                        msg = "Connection shutdown. Please check your internet"
                    }
                    is IOException -> {
                        msg = "Server is unreachable, please try again later."
                    }
                    is IllegalStateException -> {
                        msg = "${e.message}"
                    }
                    else -> {
                        msg = "${e.message}"
                    }
                }

                return Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(999)
                    .message(msg)
                    .build()
            }

        }

        private fun getClientBuilderWithToken(
            context: Context,
            chain: Interceptor.Chain,
            authorization: String
        ): Request.Builder {
            val builder = chain.request().newBuilder().addHeader("Content-Type", "application/json")
            return builder
                .addHeader("Authorization", authorization)
//            .addHeader("Content-Type", "application/json")
                .addHeader(
                    "user-index",
                    preferenceRepository.getPreference(PrefKey.KEY_USER_INDEX) ?: ""
                )
//            .addHeader("user-index", "10005")
        }

        private fun getNaverClientBuilderWithToken(
            chain: Interceptor.Chain,
            authorization: String
        ): Request.Builder {
            return chain.request().newBuilder()
//            .removeHeader("user-index")
                .addHeader("Authorization", authorization)
        }

        private fun getNaverClientBuilderNID(chain: Interceptor.Chain): Request.Builder {
            return chain.request().newBuilder()
//            .removeHeader("Authorization")
//            .removeHeader("user-index")
        }
    }

}
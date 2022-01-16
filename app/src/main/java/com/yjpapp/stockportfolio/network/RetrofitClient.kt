package com.yjpapp.stockportfolio.network

import android.content.Context
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
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
    private val preferenceController: PreferenceController
) {
    enum class BaseServerURL(val url: String) {
        MY("http://112.147.50.241"),
        NAVER_OPEN_API("https://openapi.naver.com"),
        NAVER_NID("https:Z//nid.naver.com")
    }

    private val TAG = RetrofitClient::class.java.simpleName
    private val CONNECT_TIMEOUT_OUT_MINUTE: Long = 3
    private val READ_TIMEOUT_OUT_MINUTE: Long = 3

    fun getService(context: Context, baseServerURL: BaseServerURL): RetrofitService? {
        if (NetworkUtils.isInternetAvailable(context)) {
            val interceptor: Interceptor = CustomInterceptor(context, baseServerURL, preferenceController)

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

    class CustomInterceptor(
        private val context: Context,
        private val baseServerURL: BaseServerURL,
        private val preferenceController: PreferenceController
    ) : Interceptor {
        private val TAG = CustomInterceptor::class.java.simpleName
        override fun intercept(chain: Interceptor.Chain): Response {
            val builder =
                when (baseServerURL) {
                    BaseServerURL.MY -> {
                        val authorization = preferenceController
                            .getPreference(PrefKey.KEY_USER_TOKEN) ?: ""
                        getClientBuilderWithToken(context, chain, authorization)
                    }
                    BaseServerURL.NAVER_OPEN_API -> {
                        val authorization = preferenceController
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
                    preferenceController.getPreference(PrefKey.KEY_USER_INDEX) ?: ""
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
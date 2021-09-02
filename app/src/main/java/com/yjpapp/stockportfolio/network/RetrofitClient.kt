package com.yjpapp.stockportfolio.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.yjpapp.stockportfolio.BuildConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.util.StockLog
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.android.inject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

object RetrofitClient {
    enum class BaseServerURL(val url: String) {
        MY("http://112.147.50.202"),
        NAVER("https://openapi.naver.com")
    }
    private val TAG = RetrofitClient::class.java.simpleName
    const val CONNECT_TIMEOUT_OUT_MINUTE: Long = 3
    const val READ_TIMEOUT_OUT_MINUTE: Long = 3

    fun getService(context: Context, baseServerURL: BaseServerURL, authorization: String): RetrofitService? {
        if (isInternetAvailable(context)) {
            val interceptor: Interceptor = object : Interceptor {
                @Throws(IOException::class)
                override fun intercept(chain: Interceptor.Chain): Response {
                    val builder =
                        when (baseServerURL) {
                            BaseServerURL.MY -> {
                                getClientBuilderWithToken(context, chain, authorization)
                            }
                            BaseServerURL.NAVER -> {
                                getNaverClientBuilderWithToken(chain, authorization)
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

    //인터넷 사용 가능한지 여부
    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
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

    private fun getClientBuilderWithToken(context: Context, chain: Interceptor.Chain, authorization: String): Request.Builder {
        val preferenceController = PreferenceController.getInstance(context)
        return chain.request().newBuilder()
            .addHeader("Authorization", authorization)
            .addHeader("Content-Type", "application/json")
//            .addHeader("user-index", preferenceController.getPreference(PrefKey.KEY_USER_INDEX)?: "")
            .addHeader("user-index", "10005")
    }

    private fun getNaverClientBuilderWithToken(chain: Interceptor.Chain, authorization: String): Request.Builder {
        return chain.request().newBuilder()
            .addHeader("Authorization", authorization)
    }
}
package com.yjpapp.network

import com.yjpapp.network.datasource.AlphaVantageDataSource
import com.yjpapp.network.datasource.DataPortalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    if (BuildConfig.DEBUG) {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    }
                },
        )
        .build()

    @Provides
    @Singleton
    fun providesDataPortalDataSource(
        networkJson: Json,
        okhttpCallFactory: Call.Factory,
    ): DataPortalDataSource = RetrofitDataPortal(networkJson, okhttpCallFactory)

    @Provides
    @Singleton
    fun providesAlphaVantageDataSource(
        networkJson: Json,
        okhttpCallFactory: Call.Factory,
    ): AlphaVantageDataSource = RetrofitDataPortal(networkJson, okhttpCallFactory)
}
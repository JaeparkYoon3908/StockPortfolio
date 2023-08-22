package com.yjpapp.data.di

import android.content.Context
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.network.RetrofitClient
import com.yjpapp.data.network.service.NaverNidService
import com.yjpapp.data.network.service.NaverOpenService
import com.yjpapp.data.network.service.RaspberryPiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofitClient(
        @ApplicationContext context: Context,
        preferenceRepository: PreferenceDataSource
    ): RetrofitClient {
        return RetrofitClient(context, preferenceRepository)
    }

    @Provides
    @Singleton
    fun provideNaverNidService(
        retrofitClient: RetrofitClient
    ): NaverNidService {
        val retrofit = retrofitClient.getRetrofit(RetrofitClient.BaseServerURL.NAVER_NID)
        return retrofit.create(NaverNidService::class.java)
    }

    @Provides
    @Singleton
    fun provideNaverOpenService(
        retrofitClient: RetrofitClient
    ): NaverOpenService {
        val retrofit = retrofitClient.getRetrofit(RetrofitClient.BaseServerURL.NAVER_OPEN_API)
        return retrofit.create(NaverOpenService::class.java)
    }

    @Provides
    @Singleton
    fun provideRaspberryPiService(
        retrofitClient: RetrofitClient
    ): RaspberryPiService {
        val retrofit = retrofitClient.getRetrofit(RetrofitClient.BaseServerURL.RaspberryPi)
        return retrofit.create(RaspberryPiService::class.java)
    }
}
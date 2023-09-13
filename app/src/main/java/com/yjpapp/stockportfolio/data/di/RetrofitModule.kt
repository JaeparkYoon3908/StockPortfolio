package com.yjpapp.stockportfolio.data.di

import android.content.Context
import com.yjpapp.stockportfolio.data.datasource.PreferenceDataSource
import com.yjpapp.stockportfolio.data.network.RetrofitClient
import com.yjpapp.stockportfolio.data.network.service.DataPortalService
import com.yjpapp.stockportfolio.data.network.service.NaverNidService
import com.yjpapp.stockportfolio.data.network.service.NaverOpenService
import com.yjpapp.stockportfolio.data.network.service.RaspberryPiService
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

    @Provides
    @Singleton
    fun provideDataPortalService(
        retrofitClient: RetrofitClient
    ): DataPortalService {
        val retrofit = retrofitClient.getRetrofit(RetrofitClient.BaseServerURL.DATA_PORTAL)
        return retrofit.create(DataPortalService::class.java)
    }
}
package com.yjpapp.data.di

import com.yjpapp.data.datasource.MyStockRoomDataSource
import com.yjpapp.data.datasource.NewsDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.repository.MySettingRepository
import com.yjpapp.data.repository.MySettingRepositoryImpl
import com.yjpapp.data.repository.MyStockRepository
import com.yjpapp.data.repository.MyStockRepositoryImpl
import com.yjpapp.network.datasource.DataPortalDataSource
import com.yjpapp.data.repository.NewsRepository
import com.yjpapp.data.repository.NewsRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideMyStockRepository(
        myStockLocalDataSource: MyStockRoomDataSource,
        stockInfoDataSource: DataPortalDataSource,
    ): MyStockRepository = MyStockRepositoryImpl(myStockLocalDataSource, stockInfoDataSource)

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsDataSource: NewsDataSource
    ): NewsRepository = NewsRepositoryImpl(newsDataSource)

    @Provides
    @Singleton
    fun provideMySettingRepository(
        preferenceDataSource: PreferenceDataSource
    ): MySettingRepository = MySettingRepositoryImpl(preferenceDataSource)
}
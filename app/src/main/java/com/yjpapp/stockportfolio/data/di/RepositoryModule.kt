package com.yjpapp.stockportfolio.data.di

import com.yjpapp.stockportfolio.data.datasource.MyStockRoomDataSource
import com.yjpapp.stockportfolio.data.datasource.NewsDataSource
import com.yjpapp.stockportfolio.data.datasource.StockInfoDataSource
import com.yjpapp.stockportfolio.data.repository.MyStockRepository
import com.yjpapp.stockportfolio.data.repository.MyStockRepositoryImpl
import com.yjpapp.stockportfolio.data.repository.NewsRepository
import com.yjpapp.stockportfolio.data.repository.NewsRepositoryImpl
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
        stockInfoDataSource: StockInfoDataSource
    ): MyStockRepository = MyStockRepositoryImpl(myStockLocalDataSource, stockInfoDataSource)

    @Provides
    @Singleton
    fun provideNewsRepository(
        newsDataSource: NewsDataSource
    ): NewsRepository = NewsRepositoryImpl(newsDataSource)

}
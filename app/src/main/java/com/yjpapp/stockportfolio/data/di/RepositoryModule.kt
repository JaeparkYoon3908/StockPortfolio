package com.yjpapp.stockportfolio.data.di

import com.yjpapp.stockportfolio.data.datasource.MyStockRoomDataSource
import com.yjpapp.stockportfolio.data.datasource.StockInfoDataSource
import com.yjpapp.stockportfolio.data.repository.MyStockRepository
import com.yjpapp.stockportfolio.data.repository.MyStockRepositoryImpl
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
    ): MyStockRepository {
        return MyStockRepositoryImpl(myStockLocalDataSource, stockInfoDataSource)
    }
}
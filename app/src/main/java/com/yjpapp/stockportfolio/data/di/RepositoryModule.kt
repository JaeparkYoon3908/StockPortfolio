package com.yjpapp.stockportfolio.data.di

import com.yjpapp.stockportfolio.data.datasource.MyStockDataSource
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
        myStockDataSource: MyStockDataSource
    ): MyStockRepository {
        return MyStockRepositoryImpl(myStockDataSource)
    }
}
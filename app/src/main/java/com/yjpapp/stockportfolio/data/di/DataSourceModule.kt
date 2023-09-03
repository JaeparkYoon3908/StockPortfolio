package com.yjpapp.stockportfolio.data.di

import com.yjpapp.stockportfolio.data.datasource.MyStockRoomDataSource
import com.yjpapp.stockportfolio.data.datasource.NewsDataSource
import com.yjpapp.stockportfolio.data.datasource.StockInfoDataSource
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockDao
import com.yjpapp.stockportfolio.data.network.service.DataPortalService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.xmlpull.v1.XmlPullParserFactory
import javax.inject.Singleton

/**
 * 의존성을 주입하는 부분
 */

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {
    @Provides
    @Singleton
    fun provideMyStockLocalDataSource(
        myStockDao: MyStockDao
    ): MyStockRoomDataSource = MyStockRoomDataSource(myStockDao)

    @Provides
    @Singleton
    fun provideStockPriceDataSource(
        dataPortalService: DataPortalService
    ): StockInfoDataSource = StockInfoDataSource(dataPortalService)

    @Provides
    @Singleton
    fun provideNewsDataSource()
    : NewsDataSource {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        return NewsDataSource(parser)
    }
}
package com.yjpapp.data.di

import com.yjpapp.data.datasource.MyStockRoomDataSource
import com.yjpapp.data.datasource.NewsDataSource
import com.yjpapp.data.localdb.room.mystock.MyStockDao
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
    fun provideNewsDataSource()
    : NewsDataSource {
        val factory = XmlPullParserFactory.newInstance()
        val parser = factory.newPullParser()
        return NewsDataSource(parser)
    }
}
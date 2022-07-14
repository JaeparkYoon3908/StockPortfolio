package com.yjpapp.data.di

import com.yjpapp.data.datasource.*
import com.yjpapp.data.localdb.room.MyRoomDatabase
import com.yjpapp.data.localdb.room.memo.MemoDao
import com.yjpapp.data.localdb.room.mystock.MyStockDao
import com.yjpapp.data.network.service.NaverNidService
import com.yjpapp.data.network.service.NaverOpenService
import com.yjpapp.data.network.service.RaspberryPiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * 의존성을 주입하는 부분
 */

@InstallIn(SingletonComponent::class)
@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideIncomeNoteDataSource(
        raspberryPiService: RaspberryPiService
    ): IncomeNoteDataSource {
        return IncomeNoteDataSource(raspberryPiService)
    }

    @Provides
    @Singleton
    fun provideMemoDataSource(
        memoDao: MemoDao
    ): MemoDataSource = MemoDataSource(memoDao)


    @Provides
    @Singleton
    fun provideMyStockDataSource(
        myStockDao: MyStockDao
    ): MyStockDataSource = MyStockDataSource(myStockDao)

    @Provides
    @Singleton
    fun provideUserDataSource(
        raspberryPiService: RaspberryPiService,
        naverOpenService: NaverOpenService,
        naverNIDService: NaverNidService
    ): UserDataSource {
        return UserDataSource(raspberryPiService)
    }

    @Provides
    @Singleton
    fun provideNaverOpenDataSource(
        naverOpenService: NaverOpenService
    ): NaverOpenDataSource = NaverOpenDataSource(naverOpenService)

    @Provides
    @Singleton
    fun provideNaverNIDDataSource(
        naverNIDService: NaverNidService
    ): NaverNIDDataSource = NaverNIDDataSource(naverNIDService)
}
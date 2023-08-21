package com.yjpapp.stockportfolio.data.di

import android.app.Activity
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yjpapp.stockportfolio.data.datasource.PreferenceDataSource
import com.yjpapp.stockportfolio.data.localdb.room.MyRoomDatabase
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Local db 관련 된 의존성 주입
 */
@InstallIn(SingletonComponent::class)
@Module
class LocalDatabaseModule {
    @Provides
    @Singleton
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): MyRoomDatabase =
         Room.databaseBuilder(
            context.applicationContext, MyRoomDatabase::class.java,
            MyRoomDatabase.DB_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }
            })
            .allowMainThreadQueries() //MainThread(UI Thread)에서 접근 가능할 수 있게 설정
            .fallbackToDestructiveMigration() //room table 업데이트 시 마이그레이션
            .build()


    @Provides
    @Singleton
    fun provideMyStockDao(
        roomDatabase: MyRoomDatabase
    ): MyStockDao = roomDatabase.myStockDao()

    @Provides
    @Singleton
    fun providePreferenceDataSource(
        @ApplicationContext context: Context
    ): PreferenceDataSource {
        val fileName = PreferenceDataSource.FILENAME
        val pref = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        return PreferenceDataSource(pref)
    }
}
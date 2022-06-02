package com.yjpapp.data.di

import android.app.Activity
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yjpapp.data.datasource.*
import com.yjpapp.data.localdb.room.MyRoomDatabase
import com.yjpapp.data.network.RetrofitClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
    fun provideRoomDatabase(
        @ApplicationContext context: Context
    ): MyRoomDatabase {
        return Room.databaseBuilder(
            context.applicationContext, MyRoomDatabase::class.java,
            MyRoomDatabase.DB_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }
            })
            .allowMainThreadQueries() //추천하진 않지만 MainThread(UI Thread)에서 접근 가능할 수 있게 설정
            .fallbackToDestructiveMigration() //room table 업데이트 시 마이그레이션
            .build()
    }

    @Provides
    @Singleton
    fun providePreferenceDataSource(
        @ApplicationContext context: Context
    ): PreferenceDataSource {
        val fileName = PreferenceDataSource.FILENAME
        val pref = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        return PreferenceDataSource(pref)
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(
        @ApplicationContext context: Context,
        preferenceRepository: PreferenceDataSource
    ): RetrofitClient {
        return RetrofitClient(context, preferenceRepository)
    }

    @Provides
    fun provideIncomeNoteDataSource(
        retrofitClient: RetrofitClient
    ): IncomeNoteDataSource {
        return IncomeNoteDataSource(retrofitClient)
    }

    @Provides
    fun provideMemoDataSource(
        myRoomDatabase: MyRoomDatabase,
        preferenceRepository: PreferenceDataSource
    ): MemoDataSource {
        return MemoDataSource(myRoomDatabase.memoListDao(), preferenceRepository)
    }

    @Provides
    fun provideMyRepository(
        preferenceRepository: PreferenceDataSource
    ): MyDataSource {
        return MyDataSource(preferenceRepository)
    }

    @Provides
    fun provideUserRepository(
        preferenceRepository: PreferenceDataSource,
        retrofitClient: RetrofitClient
    ): UserDataSource {
        return UserDataSource(preferenceRepository, retrofitClient)
    }

    @Provides
    fun provideMyStockRepository(
        myRoomDatabase: MyRoomDatabase
    ): MyStockDataSource {
        return MyStockDataSource(myRoomDatabase.myStockDao())
    }
}
package com.yjpapp.stockportfolio.di

import android.app.Activity
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yjpapp.stockportfolio.localdb.room.MyRoomDatabase
import com.yjpapp.stockportfolio.network.RetrofitClient
import com.yjpapp.stockportfolio.repository.*
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
class HiltModule {
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
    fun providePreferenceRepository(
        @ApplicationContext context: Context
    ): PreferenceRepository {
        val fileName = PreferenceRepository.FILENAME
        val pref = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        return PreferenceRepository(pref)
    }

    @Provides
    @Singleton
    fun provideRetrofitClient(
        preferenceRepository: PreferenceRepository
    ): RetrofitClient {
        return RetrofitClient(preferenceRepository)
    }

    @Provides
    fun provideIncomeNoteRepository(
        preferenceRepository: PreferenceRepository,
        retrofitClient: RetrofitClient
    ): IncomeNoteRepository {
        return IncomeNoteRepository(preferenceRepository, retrofitClient)
    }

    @Provides
    fun provideMemoRepository(
        myRoomDatabase: MyRoomDatabase,
        preferenceRepository: PreferenceRepository
    ): MemoRepository {
        return MemoRepository(myRoomDatabase.memoListDao(), preferenceRepository)
    }

    @Provides
    fun provideMyRepository(
        preferenceRepository: PreferenceRepository
    ): MyRepository {
        return MyRepository(preferenceRepository)
    }

    @Provides
    fun provideUserRepository(
        preferenceRepository: PreferenceRepository,
        retrofitClient: RetrofitClient
    ): UserRepository {
        return UserRepository(preferenceRepository, retrofitClient)
    }

    @Provides
    fun provideMyStockRepository(
        myRoomDatabase: MyRoomDatabase
    ): MyStockRepository {
        return MyStockRepository(myRoomDatabase.myStockDao())
    }
}
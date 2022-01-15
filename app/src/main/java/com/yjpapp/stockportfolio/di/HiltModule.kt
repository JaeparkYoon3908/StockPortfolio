package com.yjpapp.stockportfolio.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.localdb.room.MyRoomDatabase
import com.yjpapp.stockportfolio.repository.IncomeNoteRepository
import com.yjpapp.stockportfolio.repository.MemoRepository
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
    fun provideRoomDatabase(@ApplicationContext context: Context): MyRoomDatabase {
        return Room.databaseBuilder(context.applicationContext, MyRoomDatabase::class.java,
            MyRoomDatabase.DB_NAME
        )
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                }})
            .allowMainThreadQueries() //추천하진 않지만 MainThread(UI Thread)에서 접근 가능할 수 있게 설정
            .fallbackToDestructiveMigration() //room table 업데이트 시 마이그레이션
            .build()
    }

    @Provides
    @Singleton
    fun providePreferenceController(@ApplicationContext context: Context): PreferenceController {
        return PreferenceController.getInstance(context)
    }

    @Provides
    fun provideIncomeNoteRepository(): IncomeNoteRepository {
        return IncomeNoteRepository()
    }

    @Provides
    fun provideMemoRepository(myRoomDatabase: MyRoomDatabase, preferenceController: PreferenceController): MemoRepository {
        return MemoRepository(myRoomDatabase.memoListDao(), preferenceController)
    }

}
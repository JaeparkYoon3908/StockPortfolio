package com.yjpapp.database

import android.app.Activity
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.yjpapp.database.mystock.MyStockDao
import com.yjpapp.database.di.LocalDatabaseModule
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [LocalDatabaseModule::class]
)
object TestLocalDatabaseModule {
    @Singleton
    @Provides
    fun provideMyRoomDatabase(): MyRoomDatabase {
        val context = ApplicationProvider.getApplicationContext<Context>()
        return Room.inMemoryDatabaseBuilder(context, MyRoomDatabase::class.java).build()
    }

    @Provides
    @Singleton
    fun provideMyStockDao(
        roomDatabase: MyRoomDatabase
    ): MyStockDao = roomDatabase.myStockDao()

    @Provides
    @Singleton
    fun providePreferenceDataSource(): PreferenceDataSource {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val fileName = PreferenceDataSource.FILENAME
        val pref = context.getSharedPreferences(fileName, Activity.MODE_PRIVATE)
        return PreferenceDataSource(pref)
    }
}
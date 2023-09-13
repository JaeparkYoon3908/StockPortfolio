package com.yjpapp.stockportfolio.common

import android.app.Activity
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.test.core.app.ApplicationProvider
import com.yjpapp.stockportfolio.data.datasource.PreferenceDataSource
import com.yjpapp.stockportfolio.data.di.LocalDatabaseModule
import com.yjpapp.stockportfolio.data.localdb.room.MyRoomDatabase
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
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
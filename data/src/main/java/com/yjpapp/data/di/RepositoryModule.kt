package com.yjpapp.data.di

import com.yjpapp.data.datasource.*
import com.yjpapp.data.repository.*
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
    fun provideIncomeNoteRepository(
        incomeNoteDataSource: IncomeNoteDataSource,
        preferenceDataSource: PreferenceDataSource
    ): IncomeNoteRepository {
        return IncomeNoteRepositoryImpl(incomeNoteDataSource, preferenceDataSource)
    }

    @Provides
    @Singleton
    fun provideMyRepository(
        preferenceDataSource: PreferenceDataSource
    ): MyRepository {
        return MyRepositoryImpl(preferenceDataSource)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userDataSource: UserDataSource,
        naverNIDDataSource: NaverNIDDataSource,
        naverOpenDataSource: NaverOpenDataSource,
        preferenceDataSource: PreferenceDataSource
    ): UserRepository {
        return UserRepositoryImpl(userDataSource, naverNIDDataSource, naverOpenDataSource, preferenceDataSource)
    }

    @Provides
    @Singleton
    fun provideMyStockRepository(
        myStockDataSource: MyStockDataSource
    ): MyStockRepository {
        return MyStockRepositoryImpl(myStockDataSource)
    }

    @Provides
    @Singleton
    fun provideMemoRepository(
        memoDataSource: MemoDataSource,
        preferenceDataSource: PreferenceDataSource
    ): MemoRepository {
        return MemoRepositoryImpl(memoDataSource, preferenceDataSource)
    }

}
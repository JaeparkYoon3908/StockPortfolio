package com.yjpapp.data.di

import com.yjpapp.data.datasource.IncomeNoteDataSource
import com.yjpapp.data.datasource.PreferenceDataSource
import com.yjpapp.data.repository.IncomeNoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
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
        return IncomeNoteRepository(incomeNoteDataSource, preferenceDataSource)
    }
}
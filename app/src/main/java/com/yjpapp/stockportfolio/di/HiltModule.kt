package com.yjpapp.stockportfolio.di

import com.yjpapp.stockportfolio.repository.IncomeNoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 의존성을 주입하는 부분
 */

@InstallIn(SingletonComponent::class)
@Module
class HiltModule {
    @Provides
    fun provideIncomeNoteRepository(): IncomeNoteRepository {
        return IncomeNoteRepository()
    }
}
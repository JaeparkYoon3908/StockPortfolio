package com.yjpapp.stockportfolio.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.dsl.koinApplication

class StockPortfolioApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@StockPortfolioApp)
            /**
             * ViewModel
             */
            modules(loginViewModel)
            modules(myStockViewModel)
            modules(myViewModel)
            modules(incomeNoteViewModel)
            /**
             * Common
             */
            modules(preferenceController)
        }
    }
}
package com.yjpapp.stockportfolio.di

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class StockPortfolioApp: Application() {
    override fun onCreate() {
        super.onCreate()
        //다크모드 비활성화 처리
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
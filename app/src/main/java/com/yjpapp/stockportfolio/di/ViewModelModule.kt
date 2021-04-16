package com.yjpapp.stockportfolio.di

import com.yjpapp.stockportfolio.database.room.MyRoomDatabase
import com.yjpapp.stockportfolio.ui.mystock.MyStockInputDialog
import com.yjpapp.stockportfolio.ui.mystock.MyStockRepository
import com.yjpapp.stockportfolio.ui.mystock.MyStockViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mMyStockViewModel = module {
    single { MyRoomDatabase.getInstance(androidContext()).myStockDao() }
    factory { MyStockRepository(get()) }
    viewModel { MyStockViewModel(get()) }
    single {MyStockInputDialog(androidContext())}
}
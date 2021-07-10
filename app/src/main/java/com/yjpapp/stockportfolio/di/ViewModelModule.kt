package com.yjpapp.stockportfolio.di

import com.yjpapp.stockportfolio.ui.advertisement.AdViewModel
import com.yjpapp.stockportfolio.ui.mystock.MyStockRepository
import com.yjpapp.stockportfolio.ui.mystock.MyStockViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

//val mMyStockViewModel = module {
//    single { androidContext() }
//    viewModel { MyStockViewModel(get()) }
//}
//
//val adViewModel = module {
//    single { androidContext() }
//    viewModel { AdViewModel(get()) }
//}

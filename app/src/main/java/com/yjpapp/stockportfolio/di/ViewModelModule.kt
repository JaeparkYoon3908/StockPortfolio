package com.yjpapp.stockportfolio.di

import com.yjpapp.stockportfolio.function.login.LoginViewModel
import com.yjpapp.stockportfolio.repository.UserRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginViewModel = module {
    single { UserRepository() }
    viewModel { LoginViewModel(androidApplication(), get()) }
}

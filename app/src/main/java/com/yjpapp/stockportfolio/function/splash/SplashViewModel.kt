package com.yjpapp.stockportfolio.function.splash

import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.repository.MyRepository

class SplashViewModel(
    private val myRepository: MyRepository
): ViewModel() {
    fun requestInitMySetting() {
        myRepository.initMySetting()
    }
}
package com.yjpapp.stockportfolio.function.splash

import androidx.lifecycle.ViewModel
import com.yjpapp.data.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val myRepository: MyRepository
): ViewModel() {
    fun requestInitMySetting() {
        myRepository.initMySetting()
    }
}
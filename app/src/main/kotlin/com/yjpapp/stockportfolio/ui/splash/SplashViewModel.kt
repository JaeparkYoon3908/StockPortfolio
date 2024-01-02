package com.yjpapp.stockportfolio.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.data.model.ResponseResult
import com.yjpapp.data.repository.MySettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val mySettingRepository: MySettingRepository
) : ViewModel() {
    fun setIsFirstAppStart() = viewModelScope.launch {
        when (val result = mySettingRepository.getIsFirstAppRun()) {
            is ResponseResult.Success -> {
                if (result.data == false) {
                    mySettingRepository.setIsFirstAppRun(true)
                    mySettingRepository.setDefaultMyStockTitle("한국 주식")
                }
            }
            is ResponseResult.Error -> {

            }
        }
    }
}
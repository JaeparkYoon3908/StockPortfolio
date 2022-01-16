package com.yjpapp.stockportfolio.function

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.constance.StockConfig
import com.yjpapp.stockportfolio.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dmoral.toasty.Toasty
import javax.inject.Inject
import kotlin.system.exitProcess

/**
 * MainActivity 전용 ViewModel
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    fun runBackPressAppCloseEvent(mContext: Context, activity: Activity) {
        val isAllowAppClose = userRepository.isAllowAppClose()
        if (isAllowAppClose == StockConfig.TRUE) {
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        } else {
            Toasty.normal(mContext, mContext.getString(R.string.Common_BackButton_AppClose_Message)).show()
            userRepository.setAllowAppClose(StockConfig.TRUE)
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                userRepository.setAllowAppClose(StockConfig.FALSE)
            },3000)
        }
    }
}
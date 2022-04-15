package com.yjpapp.stockportfolio.function

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseViewModel
import com.yjpapp.stockportfolio.common.StockConfig
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.system.exitProcess

/**
 * MainActivity 전용 ViewModel
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val userRepository: UserRepository
): BaseViewModel() {
    fun runBackPressAppCloseEvent(activity: Activity) {
        val isAllowAppClose = userRepository.isAllowAppClose()
        if (isAllowAppClose == StockConfig.TRUE) {
            activity.finishAffinity()
            System.runFinalization()
            exitProcess(0)
        } else {
            Toasty.normal(context, context.getString(R.string.Common_BackButton_AppClose_Message)).show()
            setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.TRUE)
            viewModelScope.launch {
                delay(3000)
                setPreference(PrefKey.KEY_BACK_BUTTON_APP_CLOSE, StockConfig.FALSE)
            }
        }
    }

    fun setPreference(prefKey: String, value: String) {
        userRepository.setPreference(prefKey, value)
    }

    fun getPreference(prefKey: String): String {
        return userRepository.getPreference(prefKey)
    }

    fun isExistPreference(prefKey: String): Boolean {
        return userRepository.isExistPreference(prefKey)
    }
}
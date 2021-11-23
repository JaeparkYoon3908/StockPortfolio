package com.yjpapp.stockportfolio.function.my

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.network.ResponseAlertManger
import com.yjpapp.stockportfolio.repository.UserRepository
import com.yjpapp.stockportfolio.util.Utils
import kotlinx.coroutines.launch

class MyViewModel(
    private val preferenceController: PreferenceController,
    private val userRepository: UserRepository
): ViewModel() {
    val userName = preferenceController.getPreference(PrefKey.KEY_USER_NAME)
    val userEmail = Utils.getEmailMasking(preferenceController.getPreference(PrefKey.KEY_USER_EMAIL))
    val userLoginType = preferenceController.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)
    val isMyStockAutoRefresh = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)
    val isMyStockAutoAdd = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)
    val isMyStockShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)
    val isIncomeNoteShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)
    val isAutoLoginCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_AUTO_LOGIN)
    val isAutoMemoShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)
    val isMemberOffSuccess = MutableLiveData<Boolean>()
    fun requestLogout(context: Context) {
        userRepository.logout(context)
    }
    fun requestMemberOff(context: Context) {
        viewModelScope.launch {
            val result = userRepository.deleteUserInfo(context)
            if (result == null) {
                ResponseAlertManger.showNetworkConnectErrorAlert(context)
                return@launch
            }
            if (result.isSuccessful) {
                isMemberOffSuccess.value = true
            }
        }
    }
}
package com.yjpapp.stockportfolio.function.my

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController
import com.yjpapp.stockportfolio.repository.UserRepository
import kotlinx.coroutines.launch

class MyViewModel(
    private val preferenceController: PreferenceController,
    private val userRepository: UserRepository
): ViewModel() {
    val userName = preferenceController.getPreference(PrefKey.KEY_USER_NAME)
    val userEmail = preferenceController.getPreference(PrefKey.KEY_USER_EMAIL)
    val userLoginType = preferenceController.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)
    val isMyStockAutoRefresh = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)
    val isMyStockAutoAdd = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)
    val isMyStockShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)
    val isIncomeNoteShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)

    fun requestLogout(context: Context) {
        userRepository.logout(context)
    }
    fun requestMemberOff(context: Context, userId: Int) {
        viewModelScope.launch {
            userRepository.deleteUserInfo(context, userId)
        }
    }
}
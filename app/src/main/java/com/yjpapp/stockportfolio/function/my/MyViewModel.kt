package com.yjpapp.stockportfolio.function.my

import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController

class MyViewModel(preferenceController: PreferenceController): ViewModel() {
    val userName = preferenceController.getPreference(PrefKey.KEY_USER_NAME)
    val userEmail = preferenceController.getPreference(PrefKey.KEY_USER_EMAIL)
    val userLoginType = preferenceController.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)
    val isAutoRefresh = preferenceController.getPreference(PrefKey.KEY_SETTING_AUTO_REFRESH)
    val isAutoAdd = preferenceController.getPreference(PrefKey.KEY_SETTING_AUTO_ADD)
    val isShowDeleteCheck = preferenceController.getPreference(PrefKey.KEY_SETTING_SHOW_DELETE_CHECK)

    fun requestLogout() {

    }

    fun requestMemberOff() {

    }
}
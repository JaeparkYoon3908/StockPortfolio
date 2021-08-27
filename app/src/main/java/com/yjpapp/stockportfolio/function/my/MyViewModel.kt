package com.yjpapp.stockportfolio.function.my

import androidx.lifecycle.ViewModel
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController

class MyViewModel(preferenceController: PreferenceController): ViewModel() {
    val userName = preferenceController.getPreference(PrefKey.KEY_USER_NAME)
    val userEmail = preferenceController.getPreference(PrefKey.KEY_USER_EMAIL)
    val userLoginType = preferenceController.getPreference(PrefKey.KEY_USER_LOGIN_TYPE)
}
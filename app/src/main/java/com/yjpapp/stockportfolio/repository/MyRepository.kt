package com.yjpapp.stockportfolio.repository

import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController

class MyRepository(
    private val preferenceController: PreferenceController
) {
    fun setAutoLogin(isAutoLogin: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_AUTO_LOGIN, isAutoLogin)
    }
    fun setMyStockSetAutoRefresh(isAutoRefresh: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH, isAutoRefresh)
    }

}
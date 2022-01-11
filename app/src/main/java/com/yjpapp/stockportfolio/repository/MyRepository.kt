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
    fun setMyStockAutoAdd(isAutoAdd: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD, isAutoAdd)
    }
    fun setMyStockShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun setIncomeNoteShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun setShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun setMemoVibrateOff(isVibrateOff: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK, isVibrateOff)
    }

    fun deleteUserPreference() {
        preferenceController.removePreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)
        preferenceController.removePreference(PrefKey.KEY_AUTO_LOGIN)

        preferenceController.removePreference(PrefKey.KEY_USER_INDEX)
        preferenceController.removePreference(PrefKey.KEY_USER_LOGIN_TYPE)
        preferenceController.removePreference(PrefKey.KEY_USER_NAME)
        preferenceController.removePreference(PrefKey.KEY_USER_EMAIL)
        preferenceController.removePreference(PrefKey.KEY_USER_TOKEN)

        preferenceController.removePreference(PrefKey.KEY_SETTING_AUTO_LOGIN)
        preferenceController.removePreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)
        preferenceController.removePreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)
        preferenceController.removePreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)
        preferenceController.removePreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)
        preferenceController.removePreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)
    }
}
package com.yjpapp.data.datasource

import com.yjpapp.data.localdb.preference.PrefKey

class MyDataSource(
    private val preferenceDataSource: PreferenceDataSource
) {
    fun setAutoLogin(isAutoLogin: Boolean) {
        preferenceDataSource.setPreference(PrefKey.KEY_SETTING_AUTO_LOGIN, isAutoLogin)
    }
    fun getAutoLogin(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_SETTING_AUTO_LOGIN)?: ""
    }
    fun setMyStockAutoRefresh(isAutoRefresh: Boolean) {
        preferenceDataSource.setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH, isAutoRefresh)
    }
    fun getMyStockAutoRefresh(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)?: ""
    }
    fun setMyStockAutoAdd(isAutoAdd: Boolean) {
        preferenceDataSource.setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD, isAutoAdd)
    }
    fun getMyStockAutoAdd(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)?: ""
    }
    fun setMyStockShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceDataSource.setPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getMyStockShowDeleteCheck(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)?: ""
    }
    fun setIncomeNoteShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceDataSource.setPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getIncomeNoteShowDeleteCheck(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)?: ""
    }
    fun setShowMemoDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceDataSource.setPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getShowMemoDeleteCheck(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)?: ""
    }
    fun setMemoVibrateOff(isVibrateOff: Boolean) {
        preferenceDataSource.setPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK, isVibrateOff)
    }
    fun getMemoVibrateOff(): String {
        return preferenceDataSource.getPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)?: ""
    }
    fun deleteUserPreference() {
        preferenceDataSource.apply {
            removePreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)
            removePreference(PrefKey.KEY_AUTO_LOGIN)

            removePreference(PrefKey.KEY_USER_INDEX)
            removePreference(PrefKey.KEY_USER_LOGIN_TYPE)
            removePreference(PrefKey.KEY_USER_NAME)
            removePreference(PrefKey.KEY_USER_EMAIL)
            removePreference(PrefKey.KEY_USER_TOKEN)

            removePreference(PrefKey.KEY_SETTING_AUTO_LOGIN)
            removePreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)
            removePreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)
            removePreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)
            removePreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)
            removePreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)
        }
    }
    fun initMySetting() {
        preferenceDataSource.apply {
            if (!isExists(PrefKey.KEY_SETTING_AUTO_LOGIN)) {
                setPreference(PrefKey.KEY_SETTING_AUTO_LOGIN, true)
            }
            if (!isExists(PrefKey.KEY_AUTO_LOGIN)) {
                setPreference(PrefKey.KEY_AUTO_LOGIN, false)
            }
            //나의 주식
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK, true)
            }
            //수익 노트
            if (!isExists(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK, true)
            }
            //메모
            if (!isExists(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK, true)
            }
            if (!isExists(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)) {
                setPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK, true)
            }
        }
    }
}
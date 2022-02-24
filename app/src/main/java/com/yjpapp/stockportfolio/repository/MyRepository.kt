package com.yjpapp.stockportfolio.repository

import com.yjpapp.stockportfolio.localdb.preference.PrefKey

class MyRepository(
    private val preferenceRepository: PreferenceRepository
) {
    fun setAutoLogin(isAutoLogin: Boolean) {
        preferenceRepository.setPreference(PrefKey.KEY_SETTING_AUTO_LOGIN, isAutoLogin)
    }
    fun getAutoLogin(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_SETTING_AUTO_LOGIN)?: ""
    }
    fun setMyStockAutoRefresh(isAutoRefresh: Boolean) {
        preferenceRepository.setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH, isAutoRefresh)
    }
    fun getMyStockAutoRefresh(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)?: ""
    }
    fun setMyStockAutoAdd(isAutoAdd: Boolean) {
        preferenceRepository.setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD, isAutoAdd)
    }
    fun getMyStockAutoAdd(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)?: ""
    }
    fun setMyStockShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceRepository.setPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getMyStockShowDeleteCheck(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)?: ""
    }
    fun setIncomeNoteShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceRepository.setPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getIncomeNoteShowDeleteCheck(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)?: ""
    }
    fun setShowMemoDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceRepository.setPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getShowMemoDeleteCheck(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)?: ""
    }
    fun setMemoVibrateOff(isVibrateOff: Boolean) {
        preferenceRepository.setPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK, isVibrateOff)
    }
    fun getMemoVibrateOff(): String {
        return preferenceRepository.getPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)?: ""
    }
    fun deleteUserPreference() {
        preferenceRepository.removePreference(PrefKey.KEY_BOTTOM_MENU_SELECTED_POSITION)
        preferenceRepository.removePreference(PrefKey.KEY_AUTO_LOGIN)

        preferenceRepository.removePreference(PrefKey.KEY_USER_INDEX)
        preferenceRepository.removePreference(PrefKey.KEY_USER_LOGIN_TYPE)
        preferenceRepository.removePreference(PrefKey.KEY_USER_NAME)
        preferenceRepository.removePreference(PrefKey.KEY_USER_EMAIL)
        preferenceRepository.removePreference(PrefKey.KEY_USER_TOKEN)

        preferenceRepository.removePreference(PrefKey.KEY_SETTING_AUTO_LOGIN)
        preferenceRepository.removePreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)
        preferenceRepository.removePreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)
        preferenceRepository.removePreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)
        preferenceRepository.removePreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)
        preferenceRepository.removePreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)
    }
    fun initMySetting() {
        preferenceRepository.apply {
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
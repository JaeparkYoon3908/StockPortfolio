package com.yjpapp.stockportfolio.repository

import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController

class MyRepository(
    private val preferenceController: PreferenceController
) {
    fun setAutoLogin(isAutoLogin: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_AUTO_LOGIN, isAutoLogin)
    }
    fun getAutoLogin(): String {
        return preferenceController.getPreference(PrefKey.KEY_SETTING_AUTO_LOGIN)?: ""
    }
    fun setMyStockAutoRefresh(isAutoRefresh: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH, isAutoRefresh)
    }
    fun getMyStockAutoRefresh(): String {
        return preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_REFRESH)?: ""
    }
    fun setMyStockAutoAdd(isAutoAdd: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD, isAutoAdd)
    }
    fun getMyStockAutoAdd(): String {
        return preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_AUTO_ADD)?: ""
    }
    fun setMyStockShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getMyStockShowDeleteCheck(): String {
        return preferenceController.getPreference(PrefKey.KEY_SETTING_MY_STOCK_SHOW_DELETE_CHECK)?: ""
    }
    fun setIncomeNoteShowDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getIncomeNoteShowDeleteCheck(): String {
        return preferenceController.getPreference(PrefKey.KEY_SETTING_INCOME_NOTE_SHOW_DELETE_CHECK)?: ""
    }
    fun setShowMemoDeleteCheck(isDeleteCheckShow: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK, isDeleteCheckShow)
    }
    fun getShowMemoDeleteCheck(): String {
        return preferenceController.getPreference(PrefKey.KEY_SETTING_MEMO_SHOW_DELETE_CHECK)?: ""
    }
    fun setMemoVibrateOff(isVibrateOff: Boolean) {
        preferenceController.setPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK, isVibrateOff)
    }
    fun getMemoVibrateOff(): String {
        return preferenceController.getPreference(PrefKey.KEY_SETTING_MEMO_LONG_CLICK_VIBRATE_CHECK)?: ""
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
    fun initMySetting() {
        preferenceController.apply {
            if (!isExists(PrefKey.KEY_SETTING_AUTO_LOGIN)) {
                setPreference(PrefKey.KEY_SETTING_AUTO_LOGIN, true)
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
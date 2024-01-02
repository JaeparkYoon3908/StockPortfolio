package com.yjpapp.data.datasource

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(
    private val pref: SharedPreferences
) {
    companion object {
        const val FILENAME = "prefs"
    }
    fun setPreference(key: String, value: String?) {
        val editor = pref.edit()
        editor.putString(key, value).apply()
        editor.apply()
    }

    fun setPreference(key: String, value: Boolean) {
        val editor = pref.edit()
        editor.putString(key, value.toString()).apply()
        editor.apply()
    }

    fun setPreference(key: String, value: Int) {
        val editor = pref.edit()
        editor.putString(key, value.toString()).apply()
        editor.apply()
    }

    fun setPreference(key: String, value: Long) {
        val editor = pref.edit()
        editor.putString(key, value.toString()).apply()
        editor.apply()
    }

    fun getPreference(key: String): String =
        pref.getString(key, "")?: ""

    fun getPreference(key: String, defValue: String): String =
        pref.getString(key, defValue)?: defValue

    fun removePreference(key: String) {
        val editor = pref.edit()
        editor.remove(key)
        editor.apply()
        editor.commit()
    }

    fun clearPreference() {
        try {
            val editor = pref.edit()
            editor.clear()
            editor.apply()
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    fun isExists(key: String): Boolean = pref.contains(key)
}
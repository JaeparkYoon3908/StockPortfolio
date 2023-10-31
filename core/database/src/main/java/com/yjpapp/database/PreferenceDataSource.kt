package com.yjpapp.database

import android.content.SharedPreferences
import javax.inject.Inject

class PreferenceDataSource @Inject constructor(
    private val pref: SharedPreferences
) {

    companion object {
        const val FILENAME = "prefs"
    }
    fun setPreference(key: String, value: String?) {
        synchronized(this) {
            try {
                val editor = pref.edit()
                editor.putString(key, value).apply()
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setPreference(key: String, value: Boolean) {
        synchronized(this) {
            try {
                val editor = pref.edit()
                editor.putString(key, value.toString()).apply()
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setPreference(key: String, value: Int) {
        synchronized(this) {
            try {
                val editor = pref.edit()
                editor.putString(key, value.toString()).apply()
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun setPreference(key: String, value: Long) {
        synchronized(this) {
            try {
                val editor = pref.edit()
                editor.putString(key, value.toString()).apply()
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPreference(key: String): String? {
        synchronized(this) {
            return try {
                pref.getString(key, "").toString()
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    fun getPreference(key: String, defValue: String): String {
        synchronized(this) {
            return try {
                pref.getString(key, defValue).toString()
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
    }

    fun removePreference(key: String) {
        synchronized(this) {
            val editor = pref.edit()
            editor.remove(key)
            editor.apply()
            editor.commit()
        }
    }

    fun clearPreference() {
        synchronized(this) {
            try {
                val editor = pref.edit()
                editor.clear()
                editor.apply()
                editor.apply()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun isExists(key: String): Boolean {
        var isExists = false
        try {
            isExists = pref.contains(key)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            return isExists
        }
    }
}
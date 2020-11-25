package com.yjpapp.stockportfolio.preference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

private const val FILENAME = "prefs"

class PreferenceUtil {
    companion object {
        @Volatile private var instance: PreferenceUtil? = null
        private lateinit var mContext: Context
        private lateinit var pref: SharedPreferences

        @JvmStatic
        fun getInstance(context: Context): PreferenceUtil =
            instance ?: synchronized(this) {
                instance ?: PreferenceUtil().also {
                    instance = it
                    mContext = context
                    pref = mContext.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE)
                }
            }
    }
    //TODO Preference Synchronized 적용
    public fun setPreference(key: String, value: String){
        try{
            val editor = pref.edit()
            editor.putString(key, value).apply()
            editor.commit()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}
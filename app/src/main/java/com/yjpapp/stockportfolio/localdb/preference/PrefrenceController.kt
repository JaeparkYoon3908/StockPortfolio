package com.yjpapp.stockportfolio.localdb.preference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

private const val FILENAME = "prefs"

class PreferenceController {
    companion object {
        @Volatile private var instance: PreferenceController? = null
//        private lateinit var mContext: Context
        private lateinit var pref: SharedPreferences

        @JvmStatic
        fun getInstance(context: Context): PreferenceController =
            instance ?: synchronized(this) {
                instance ?: PreferenceController().also {
                    instance = it
//                    mContext = context
                    pref = context.getSharedPreferences(FILENAME, Activity.MODE_PRIVATE)
                }
            }
    }

    fun setPreference(key: String, value: String?){
        synchronized(this){
            try{
                val editor = pref.edit()
                editor.putString(key, value).apply()
                editor.commit()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun setPreference(key: String, value: Boolean){
        synchronized(this){
            try{
                val editor = pref.edit()
                editor.putString(key, value.toString()).apply()
                editor.commit()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun setPreference(key: String, value: Int){
        synchronized(this){
            try{
                val editor = pref.edit()
                editor.putString(key, value.toString()).apply()
                editor.commit()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun setPreference(key: String, value: Long){
        synchronized(this){
            try{
                val editor = pref.edit()
                editor.putString(key, value.toString()).apply()
                editor.commit()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getPreference(key: String): String?{
        synchronized(this){
            return try {
                pref.getString(key, "").toString()
            }catch (e: Exception){
                e.printStackTrace()
                null
            }
        }
    }

    fun getPreference(key: String, defValue: String): String{
        synchronized(this){
            return try {
                pref.getString(key, defValue).toString()
            }catch (e: Exception){
                e.printStackTrace()
                ""
            }
        }
    }

    fun removePreference(key: String){
        synchronized(this){
            val editor = pref.edit()
            editor.remove(key)
            editor.apply()
            editor.commit()
        }
    }

    fun clearPreference(){
        synchronized(this){
            try {
                val editor = pref.edit()
                editor.clear()
                editor.apply()
                editor.commit()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun isExists(key: String): Boolean{
        var isExists = false
        try{
            isExists = pref.contains(key)
        }catch (e: Exception){
            e.printStackTrace()
        }finally {
            return isExists
        }
    }
}
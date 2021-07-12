package com.yjpapp.stockportfolio.ui.login

import android.content.Context
import android.content.Intent
import com.yjpapp.stockportfolio.localdb.preference.PrefKey
import com.yjpapp.stockportfolio.localdb.preference.PreferenceController

class SNSLogoutManager {
    private val TAG = SNSLogoutManager::class.java.simpleName
    companion object {
        @Volatile private var instance:SNSLogoutManager? = null

        @JvmStatic
        fun getInstance(): SNSLogoutManager =
            instance ?: synchronized(this) {
                instance ?: SNSLogoutManager().also {
                    instance = it
                }
            }

    }
    fun logout(context: Context) {
        //프리퍼런스 reset
        PreferenceController.getInstance(context).setPreference(PrefKey.KEY_AUTO_LOGIN, false)
        PreferenceController.getInstance(context).setPreference(PrefKey.KEY_USER_INDEX, "")
        PreferenceController.getInstance(context).setPreference(PrefKey.KEY_USER_TOKEN, "")

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    fun cancelMember(){

    }
}
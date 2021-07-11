package com.yjpapp.stockportfolio.ui.login

import android.content.Context
import android.content.Intent

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
        //TODO 프리퍼런스 추가
        val intent = Intent(context, LoginActivity::class.java)
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK, Intent.FLAG_ACTIVITY_SINGLE_TOP)
        context.startActivity(intent)
    }

    fun cancelMember(){

    }
}
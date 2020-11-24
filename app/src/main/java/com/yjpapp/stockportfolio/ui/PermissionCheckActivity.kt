package com.yjpapp.stockportfolio.ui

import android.content.Intent
import android.os.Bundle
import com.yjpapp.stockportfolio.R

class PermissionCheckActivity: BaseActivity(R.layout.activity_permission_check) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()
    }

    private fun checkPermission(){
        val intent = Intent(mContext, SplashActivity::class.java)
        startActivity(intent)
    }
}
package com.yjpapp.stockportfolio.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.BaseActivity
import com.yjpapp.stockportfolio.ui.MainActivity

class SplashActivity: BaseActivity(R.layout.activity_splash) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startSplash()
    }

    override fun initData() {
    }

    override fun initLayout() {
    }

    private fun startSplash(){
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(mContext, MainActivity::class.java)
            finish()
            startActivity(intent)
        },1500)
    }
}
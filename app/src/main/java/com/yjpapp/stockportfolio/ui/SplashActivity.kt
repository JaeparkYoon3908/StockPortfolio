package com.yjpapp.stockportfolio.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.yjpapp.stockportfolio.base.BaseMVPActivity
import com.yjpapp.stockportfolio.databinding.ActivitySplashBinding

/**
 * 앱 실행 시 오프닝 화면
 *
 * @author Yoon Jae-park
 * @since 2020.10
 */

class SplashActivity: BaseMVPActivity<ActivitySplashBinding>() {
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

    override fun getViewBinding(): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}
package com.yjpapp.stockportfolio.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.yjpapp.stockportfolio.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


/**
 * 앱 실행 시 오프닝 화면
 *
 * @author Yoon Jae-park
 * @since 2020.10
 */

@AndroidEntryPoint
class SplashActivity: ComponentActivity() {

    private val PERMISSION_REQUEST_CODE = 0 // 임의 설정

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
        startMainActivity()
    }

    private fun startMainActivity() {
        lifecycleScope.launch {
            val intent = Intent(applicationContext, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }
}
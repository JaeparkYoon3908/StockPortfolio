package com.yjpapp.stockportfolio.test

import android.os.Bundle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.databinding.ActivityTestBinding

/**
 * 테스트 전용 Activity
 */
class TestActivity : BaseActivity<ActivityTestBinding>(R.layout.activity_test) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
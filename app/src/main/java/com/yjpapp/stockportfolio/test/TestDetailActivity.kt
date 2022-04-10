package com.yjpapp.stockportfolio.test

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.databinding.ActivityTestDetailBinding

class TestDetailActivity: BaseActivity() {
    private var _binding: ActivityTestDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_test_detail)
        super.onCreate(savedInstanceState)
    }
}
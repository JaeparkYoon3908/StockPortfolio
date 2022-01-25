package com.yjpapp.stockportfolio.test

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.ActivityTestBinding
import com.yjpapp.stockportfolio.function.MainActivity
import kotlinx.coroutines.launch

/**
 * 테스트 전용 Activity
 */
class TestActivity : AppCompatActivity() {
    private val TAG = TestActivity::class.java.simpleName
    private var _binding: ActivityTestBinding? = null
    private val binding get() = _binding!!
    private val testViewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_test)
        initData()
    }

    private fun initData() {
        binding.callBack = callBack
    }

    interface CallBack {
        fun onClick(view: View)
    }

    private val callBack = object : CallBack {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.button -> {
                    testViewModel.activityDataSendText = "Change text from activity"

                    //Fragment 띄우기기
                   supportFragmentManager
                        .beginTransaction()
                        .add(
                            R.id.fragmentContainerView,
                            TestFragment(),
                            TestFragment::class.java.simpleName
                        )
                        .commit()
                }
            }
        }
    }
}
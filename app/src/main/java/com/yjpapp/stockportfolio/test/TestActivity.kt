package com.yjpapp.stockportfolio.test

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.base.BaseActivity
import com.yjpapp.stockportfolio.databinding.ActivityTestBinding
import com.yjpapp.stockportfolio.function.MainActivity
import com.yjpapp.stockportfolio.function.mystock.search.StockSearchFragment
import com.yjpapp.stockportfolio.test.model.LatestNewsUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * 테스트 전용 Activity
 */
@AndroidEntryPoint
class TestActivity : BaseActivity<ActivityTestBinding>(R.layout.activity_test) {
    private val TAG = TestActivity::class.java.simpleName
    private val testViewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        binding.callBack = callBack

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                testViewModel.uiState.collect { uiState ->
                    uiState.userMessages.firstOrNull()?.let { userMessage ->
                        // TODO: Show Snackbar with userMessage.
                        // Once the message is displayed and
                        // dismissed, notify the ViewModel.
                        Toast.makeText(this@TestActivity, uiState.userMessages.firstOrNull()?.message, Toast.LENGTH_SHORT).show()
                        testViewModel.userMessageShown(userMessage.id)
                    }
                }
            }
        }

//        testViewModel.liveData.observe(this@TestActivity, { message ->
//            Toast.makeText(this@TestActivity, message, Toast.LENGTH_SHORT).show()
//        })

//        testViewModel.eventLiveData.observe(this@TestActivity, {
//            it.getContentIfNotHandled()?.let { message ->
//                Toast.makeText(this@TestActivity, message, Toast.LENGTH_SHORT).show()
//            }
//        })

    }

    override fun onResume() {
        super.onResume()

//        testViewModel.liveData.observe(this@TestActivity, { message ->
//            Toast.makeText(this@TestActivity, message, Toast.LENGTH_SHORT).show()
//        })

        testViewModel.eventLiveData.observe(this@TestActivity, {
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this@TestActivity, message, Toast.LENGTH_SHORT).show()
            }
        })
        testViewModel.refreshNewLiveData("send onResume()")
    }
    interface CallBack {
        fun onClick(view: View)
    }

    private val callBack = object : CallBack {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.button -> {
//                    testViewModel.activityDataSendText = "Change text from activity"
//
//                    //Fragment 띄우기기
//                    supportFragmentManager
//                        .beginTransaction()
//                        .add(
//                            R.id.fragmentContainerView,
//                            StockSearchFragment(),
//                            StockSearchFragment::class.java.simpleName
//                        )
//                        .commit()
//                    binding.button.visibility = View.GONE

//                    lifecycleScope.launch {
//                        delay(3000)
//                        testViewModel.refreshNews(this@TestActivity,  "1번 메시지")
//                        testViewModel.refreshNews(this@TestActivity,  "2번 메시지")

                        testViewModel.refreshNewLiveData("1번 메시지")
                        testViewModel.refreshNewLiveData("2번 메시지")
//                    }

//                    testViewModel.refreshNewSingleLiveData("1번 메시지")
//                    testViewModel.refreshNewSingleLiveData("2번 메시지")

                }
                R.id.button_2 -> {
                    val intent = Intent(this@TestActivity, TestDetailActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
}
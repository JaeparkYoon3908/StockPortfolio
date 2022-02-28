package com.yjpapp.stockportfolio.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.databinding.ActivityTestBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import java.lang.ref.WeakReference

/**
 * 테스트 전용 Activity
 */
@AndroidEntryPoint
class TestActivity : AppCompatActivity() {
    private val TAG = TestActivity::class.java.simpleName
    private val testViewModel: TestViewModel by viewModels()
    private var _binding: ActivityTestBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_test)

        initData()
    }

    private fun initData() {
        binding.callBack = callBack

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                testViewModel.uiState.collect { uiState ->

                }
            }
        }

        testViewModel.eventLiveData.observe(this@TestActivity) { message ->
//            showToast(this@TestActivity, "반복 토스트", Toast.LENGTH_SHORT)
            Toast.makeText(this@TestActivity, "반복 토스트", Toast.LENGTH_SHORT).show()
            Toast.makeText(this@TestActivity, "반복 토스트 2번", Toast.LENGTH_SHORT).show()
        }

//        testViewModel.liveData.observe(this@TestActivity, { message ->
//            Toast.makeText(this@TestActivity, message, Toast.LENGTH_SHORT).show()
//        })
    }

    override fun onResume() {
        super.onResume()

        testViewModel.refreshNewLiveData("send onResume()")
    }

    interface CallBack {
        fun onClick(view: View)
    }
    val codeArray = arrayListOf("373220", "055550", "051905", "005935", "029960", "363280", "068270", "105560", "035420", "016360")
    var text = StringBuilder()
    private val callBack = object : CallBack {
        override fun onClick(view: View) {
            when (view.id) {
                R.id.button -> {
                    lifecycleScope.launch {
                        repeat(codeArray.size) {
                            val url = "https://finance.naver.com/item/main.naver?code=${codeArray[it]}"
                            val doc = withContext(Dispatchers.IO) {
                                Jsoup.connect(url).get()
                            }
                            val blind = doc.select(".blind")
                            if (blind.isNotEmpty() && blind.size > 19) {
                                var currentPrice = blind[15].text()
                                var dayToDayPrice = blind[16].text()
                                var dayToDayPercent = blind[17].text()
                                var yesterdayPrice = blind[18].text()
                                if (blind.size == 34) {
                                    currentPrice = blind[16].text()
                                    dayToDayPrice = blind[17].text()
                                    dayToDayPercent = blind[18].text()
                                    yesterdayPrice = blind[19].text()

                                }
                                text.append(currentPrice).append("\n")
                            }
                        }
                        binding.textView.text = text.toString()
                    }

                }
                R.id.button_2 -> {
                    val intent = Intent(this@TestActivity, TestDetailActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }
    private var mToast: WeakReference<Toast>? = null
    private val mLock = Any()
    private fun showToast(context: Context?, text: CharSequence?, duration: Int) {
        synchronized(mLock) {
            if (mToast != null && mToast!!.get() != null) {
                return
            }
            if (context == null) {
                return
            }

            val toast = Toast.makeText(context, text, duration)
            toast.show()
            lifecycleScope.launch {
                delay(duration.toLong())
                mToast = null
            }
            mToast = WeakReference<Toast>(toast)
        }
    }

}
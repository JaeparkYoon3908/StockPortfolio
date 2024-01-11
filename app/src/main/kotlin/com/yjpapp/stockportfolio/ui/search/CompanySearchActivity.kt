package com.yjpapp.stockportfolio.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.yjpapp.data.model.response.StockPriceData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author Yoon Jae-park
 * @since 2021.11
 */
@AndroidEntryPoint
class CompanySearchActivity : AppCompatActivity() {
    private val viewModel: CompanySearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val type = intent?.getIntExtra("type", 1)?: 1
        subscribeEvent()
        setContent {
            CompanySearchScreen(
                type = type,
                viewModel = viewModel,
                onItemClick = { stockPriceData ->
                    setResultStockData(stockPriceData)
                }
            )
        }
    }
    private fun subscribeEvent() {
        //미국 주식 검색 후 선택 완료 이벤트
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.usaStockInfoState.collectLatest { stockPriceData ->
                    setResultStockData(stockPriceData)
                }
            }
        }
    }

    private fun setResultStockData(stockPriceData: StockPriceData) {
        val intent = Intent().apply {
            putExtra("stockPriceInfo", stockPriceData)
        }
        setResult(RESULT_OK, intent)
        finish()
    }
}
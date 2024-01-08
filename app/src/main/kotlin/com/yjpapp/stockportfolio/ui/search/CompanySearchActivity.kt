package com.yjpapp.stockportfolio.ui.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint

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
        setContent {
            CompanySearchScreen(
                type = type,
                viewModel = viewModel,
                onItemClick = { stockPriceInfo ->
                    val intent = Intent().apply {
                        putExtra("stockPriceInfo", stockPriceInfo)
                    }
                    setResult(RESULT_OK, intent)
                    finish()
                }
            )
        }
    }
}
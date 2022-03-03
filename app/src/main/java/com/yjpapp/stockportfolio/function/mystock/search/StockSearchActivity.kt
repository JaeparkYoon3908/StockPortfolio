package com.yjpapp.stockportfolio.function.mystock.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.yjpapp.stockportfolio.common.theme.Color_222222
import dagger.hilt.android.AndroidEntryPoint

/**
 * @link 주식 추가 다이얼로그 -> 회사명 입력 할 때 띄워지는 Fragment
 *
 * @author Yoon Jae-park
 * @since 2021.11
 */
@AndroidEntryPoint
class StockSearchActivity : AppCompatActivity() {
    private val TAG = StockSearchActivity::class.java.simpleName
    private val viewModel: StockSearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                Top()
            }
        }
    }
    @Preview
    @Composable
    private fun Top() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "StockSearchFragment",
                fontSize = 16.sp,
                color = Color_222222
            )
        }
    }
}
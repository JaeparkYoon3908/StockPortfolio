package com.yjpapp.stockportfolio.function.mystock.search

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.yjpapp.stockportfolio.common.theme.Color_222222
import com.yjpapp.stockportfolio.common.theme.Color_4876C7
import com.yjpapp.stockportfolio.common.theme.Color_CD4632
import com.yjpapp.stockportfolio.common.theme.Color_FFFFFF
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
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color_FFFFFF)
            ){
                SearchBar()
            }
        }
    }
    @Preview
    @Composable
    private fun SearchBar() {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .background(Color_FFFFFF)
        ) {
            var searchKeyWord by remember { mutableStateOf("")}
            TextField(
                value = searchKeyWord,
                onValueChange = {
                    searchKeyWord = it
                },
                maxLines = 1,
                singleLine = true,
                trailingIcon = {
                    Icon(Icons.Filled.Search, "", tint = Color_222222)
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color_FFFFFF,
                    focusedIndicatorColor = Color_222222,
                    cursorColor = Color_222222
                ),
                modifier = Modifier
                    .fillMaxWidth()

            )
        }
    }

    @Preview
    @Composable
    private fun SearchList() {
        LazyColumn {

        }
    }
}
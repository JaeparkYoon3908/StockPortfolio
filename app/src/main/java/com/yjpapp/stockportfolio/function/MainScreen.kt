package com.yjpapp.stockportfolio.function

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun MainScreen(
    viewModel: MainViewModel,
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "title") }
            )
        },
        bottomBar = {
            Row {
                Text(text = "나의 주식")
                Text(text = "마이 메뉴")
                Text(text = "수익 얼마")
            }
        }
    ) {
        Surface(
            modifier = Modifier.padding(it),
            color = Color(0xff222222)
        ) {
            Text(text = "22")
        }
    }
}
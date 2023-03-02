package com.yjpapp.stockportfolio.function

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yjpapp.stockportfolio.R

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "title") }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.height(50.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_my_stock), contentDescription = null)
                    Text(text = stringResource(id = R.string.MyStockFragment_Title))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_income_note), contentDescription = null)
                    Text(text = stringResource(id = R.string.IncomeNoteFragment_Title))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_memo_list), contentDescription = null)
                    Text(text = stringResource(id = R.string.MemoListFragment_Title))
                }
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_my), contentDescription = null)
                    Text(text = stringResource(id = R.string.MyFragment_Title))
                }
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

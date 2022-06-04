package com.yjpapp.stockportfolio.function.mystock.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import com.yjpapp.data.model.SubjectName
import com.yjpapp.stockportfolio.common.theme.Color_222222
import com.yjpapp.stockportfolio.common.theme.Color_FFFFFF
import com.yjpapp.stockportfolio.util.StockLog
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
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
                    .background(Color_FFFFFF)
            ) {
                SearchBar()
                SearchList()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.initAllStockList()
    }

    @OptIn(ExperimentalComposeUiApi::class)
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
            var userInputKeyWord by remember { mutableStateOf("") }
            var showClearIcon by remember { mutableStateOf(false) }
            val keyboardController = LocalSoftwareKeyboardController.current
            val focusRequester = remember { FocusRequester() }
            TextField(
                value = userInputKeyWord,
                onValueChange = {
                    userInputKeyWord = it
                    showClearIcon = userInputKeyWord.isNotEmpty()
                },
                maxLines = 1,
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        onClick = {
                            viewModel.requestSearchList(userInputKeyWord)
                            keyboardController?.hide()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Search,
                            contentDescription = "",
                            tint = Color_222222
                        )
                    }
                },
                trailingIcon = {
                    if (showClearIcon) {
                        IconButton(onClick = { userInputKeyWord = "" }) {
                            Icon(
                                imageVector = Icons.Rounded.Clear,
                                tint = MaterialTheme.colors.onBackground,
                                contentDescription = "Clear icon"
                            )
                        }
                    }
                },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color_FFFFFF,
                    focusedIndicatorColor = Color_222222,
                    cursorColor = Color_222222
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        StockLog.d(TAG, "onSearch")
                    },
                    onDone = {
                        StockLog.d(TAG, "onDone")
                        viewModel.requestSearchList(userInputKeyWord)
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }

    @Preview
    @Composable
    private fun SearchList() {
        LazyColumn {
            items(
                count = viewModel.searchResult.size
            ) {
                SearchListItem(subjectName = viewModel.searchResult[it])
            }
        }
    }

    @Composable
    private fun SearchListItem(subjectName: SubjectName) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
                .background(Color_FFFFFF)
        ) {
            Text(
                text = subjectName.text,
                fontSize = 16.sp,
                color = Color_222222,
                modifier = Modifier
                    .clickable {
                        val intent = Intent().apply {
                            putExtra("subjectName", subjectName)
                        }
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    .fillMaxWidth()
            )
        }
    }
}
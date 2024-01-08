package com.yjpapp.stockportfolio.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yjpapp.data.model.response.StockPriceData
import com.yjpapp.stockportfolio.model.Country
import com.yjpapp.stockportfolio.ui.common.componant.LoadingWidget
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CompanySearchScreen(
    type: Int = 1,
    viewModel: CompanySearchViewModel = hiltViewModel(),
    onItemClick: (stockPriceInfo: StockPriceData) -> Unit
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color_FFFFFF)
    ) {
        /**
         * SearchBar()
         */
        Spacer(modifier = Modifier.size(10.dp))
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
                            when (type) {
                                Country.Korea.type -> {
                                    viewModel.requestKoreaSearchList(userInputKeyWord)
                                }
                                Country.Usa.type -> {
                                    viewModel.requestUsaSearchList(userInputKeyWord)
                                }
                            }
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

                    },
                    onDone = {
                        when (type) {
                            Country.Korea.type -> {
                                viewModel.requestKoreaSearchList(userInputKeyWord)
                            }
                            Country.Usa.type -> {
                                viewModel.requestUsaSearchList(userInputKeyWord)
                            }
                        }

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
        /**
         * SearchList
         */
        val companyList = if (type == Country.Korea.type) {
            uiState.koreaStockSearchResult
        } else {
            uiState.usaStockSearchResult
        }
        LazyColumn {
            items(count = companyList.size) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, bottom = 15.dp)
                        .background(Color_FFFFFF)
                ) {
                    Text(
                        text = if (type == Country.Korea.type) uiState.koreaStockSearchResult[it].itmsNm else uiState.usaStockSearchResult[it].name,
                        fontSize = 16.sp,
                        color = Color_222222,
                        modifier = Modifier
                            .clickable {
                                when (type) {
                                    Country.Korea.type -> onItemClick(uiState.koreaStockSearchResult[it])
                                    Country.Usa.type -> {

                                    }
                                }

                            }
                            .fillMaxWidth()
                    )
                }
            }
            item {
                if (type == Country.Usa.type) {
                    Text(
                        modifier = Modifier.fillMaxWidth() ,
                        text = "${Country.Usa.title} 검색\n",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            LoadingWidget()
        }
    }
}

@Preview
@Composable
private fun Preview() {
    CompanySearchScreen{}
}
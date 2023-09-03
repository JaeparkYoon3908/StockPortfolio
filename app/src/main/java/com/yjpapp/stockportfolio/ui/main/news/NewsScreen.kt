package com.yjpapp.stockportfolio.ui.main.news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yjpapp.stockportfolio.ui.common.componant.LoadingWidget
import com.yjpapp.stockportfolio.ui.common.componant.TabWidget
import com.yjpapp.stockportfolio.ui.main.MainViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val pagerState = rememberPagerState(pageCount = { viewModel.newsMenuList.size })
    val isLoading = viewModel.isLoading.collectAsStateWithLifecycle()
    Column(modifier = modifier) {
        TabWidget(
            modifier = Modifier.fillMaxWidth(),
            menus = viewModel.newsMenuList,
            pagerState = pagerState,
        ) {
            HorizontalPager(
                state = pagerState,
            ) { page ->
                val newsList = when(page) {
                    0 -> viewModel.newsUIData.mKNewsList
                    1 -> viewModel.newsUIData.hanKyungNewsList
                    2 -> viewModel.newsUIData.financialNewsList
                    else -> viewModel.newsUIData.mKNewsList
                }
                if (isLoading.value) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LoadingWidget()
                    }
                } else {
                    LazyColumn {
                        item { Spacer(modifier = Modifier.size(20.dp)) }
                        items(items = newsList) { item ->
                            NewsListItem(
                                data = item,
                                onItemClick = {
                                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
package com.yjpapp.stockportfolio.ui.main.news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yjpapp.stockportfolio.ui.main.MainViewModel

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val newsListState = viewModel.newsList.collectAsStateWithLifecycle()
    LazyColumn{
        item { Spacer(modifier = Modifier.size(20.dp)) }
        items(items = newsListState.value) { item ->
            NewsListItem(
                data = item,
                onItemClick = {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
                }
            )
        }
    }
}
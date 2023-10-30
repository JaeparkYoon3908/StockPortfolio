package com.yjpapp.stockportfolio.ui.common.componant

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yjpapp.stockportfolio.model.TabData
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_666666
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TabWidget(
    modifier: Modifier = Modifier,
    menus: List<TabData>,
    pagerState: PagerState,
    tabBackgroundColor: Color = Color_FFFFFF,
    selectedContentColor: Color = Color_222222,
    unselectedContentColor: Color = Color_666666,
    height: Dp = 1.5.dp,
    indicator: @Composable (tabPositions: List<TabPosition>) -> Unit = { tabPositions ->
        TabRowDefaults.Indicator(
            modifier = Modifier.tabIndicatorOffset(tabPositions[pagerState.currentPage]),
            height = height,
            color = Color_222222
        )
    },
    divider: @Composable () -> Unit = {},
    content: @Composable ((state: PagerState) -> Unit) = {}
) {
    val scope = rememberCoroutineScope()
    TabRow(
        modifier = Modifier
            .background(color = tabBackgroundColor)
            .then(modifier),
        selectedTabIndex = pagerState.currentPage,
        containerColor = tabBackgroundColor,
        indicator = { tabPositions -> indicator(tabPositions) },
        divider = divider
    ) {
        menus.forEachIndexed { index, tabData ->
            Tab(
                modifier = Modifier.height(56.dp),
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                selectedContentColor = selectedContentColor,
                unselectedContentColor = unselectedContentColor
            ) {
                Text(
                    text = tabData.title,
                    color = if (pagerState.currentPage == index) selectedContentColor else unselectedContentColor,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
    content(pagerState)
}


@Preview
@Composable
private fun Preview() {
}
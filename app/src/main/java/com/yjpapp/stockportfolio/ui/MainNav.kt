@file:OptIn(ExperimentalMaterialApi::class)

package com.yjpapp.stockportfolio.ui

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.mystock.MyStockScreen
import com.yjpapp.stockportfolio.ui.mystock.MyStockViewModel

@Stable
sealed class NavItem(
    val title: Int,
    val icon: Int,
    val screenRoute: String
) {
    data object MyStock: NavItem(
        title = R.string.MyStockFragment_Title,
        icon = R.drawable.ic_my_stock,
        screenRoute = "MY_STOCK"
    )

    data object News: NavItem(
        title = R.string.News_Title,
        icon = R.drawable.ic_memo_list,
        screenRoute = "NEWS",
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = NavItem.MyStock.screenRoute) {
        composable(NavItem.MyStock.screenRoute) {
            val myStockViewModel = hiltViewModel<MyStockViewModel>()
            MyStockScreen(
                modifier = modifier,
                viewModel = myStockViewModel
            )
        }
        composable(NavItem.News.screenRoute) {

        }
    }
}

fun NavController.navigation(item: NavItem) {
    navigate(item.screenRoute) {
        graph.startDestinationRoute?.let { screen_route ->
            popUpTo(screen_route) { saveState = true }
        }
        launchSingleTop = true
        restoreState = true
    }
}
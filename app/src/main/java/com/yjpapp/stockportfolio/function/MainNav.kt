@file:OptIn(ExperimentalMaterialApi::class)

package com.yjpapp.stockportfolio.function

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.function.mystock.MyStockScreen
import com.yjpapp.stockportfolio.function.mystock.MyStockViewModel

sealed class NavItem(
    val title: Int,
    val icon: Int,
    val screenRoute: String
) {
    object MyStock: NavItem(
        title = R.string.MyStockFragment_Title,
        icon = R.drawable.ic_my_stock,
        screenRoute = "MY_STOCK"
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = NavItem.MyStock.screenRoute) {
        composable(NavItem.MyStock.screenRoute) {
            val myStockViewModel = hiltViewModel<MyStockViewModel>()
            MyStockScreen(viewModel = myStockViewModel)
        }
    }
}
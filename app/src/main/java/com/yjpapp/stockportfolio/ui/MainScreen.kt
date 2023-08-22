package com.yjpapp.stockportfolio.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yjpapp.stockportfolio.common.theme.Color_222222
import com.yjpapp.stockportfolio.common.theme.Color_888888

@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val navController = rememberNavController()
    val navItemList = remember { mutableStateListOf(NavItem.MyStock, NavItem.News) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var titleText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(text = titleText)
                    }
                },
                backgroundColor = Color.White,
                contentColor = Color_222222
            )
        },
        bottomBar = {
            BottomNavigation(
                modifier = Modifier.height(60.dp),
                backgroundColor = Color.White,
                contentColor = Color_222222,
                elevation = 12.dp
            ) {
                navItemList.forEach { item ->
                    val tintColor = if (currentRoute == item.screenRoute) {
                        Color_222222
                    } else {
                        Color_888888
                    }
                    if (currentRoute == item.screenRoute) {
                        titleText = stringResource(id = item.title)
                    }
                    BottomNavigationItem(
                        selected = currentRoute == item.screenRoute,
                        onClick = {
                            navController.navigation(item)
                        },
                        icon = {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                painter = painterResource(id = item.icon),
                                tint = tintColor,
                                contentDescription = null
                            )
                        },
                        label = {
                            Text(text = stringResource(id = item.title))
                        },
                        selectedContentColor = Color_222222,
                        unselectedContentColor = Color_888888
                    )
                }
            }
        }
    ) { paddingValues ->
        NavigationGraph(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

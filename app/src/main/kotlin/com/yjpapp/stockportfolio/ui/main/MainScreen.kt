package com.yjpapp.stockportfolio.ui.main

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.common.componant.LoadingWidget
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_888888
import com.yjpapp.stockportfolio.ui.common.theme.Color_F1F1F1
import com.yjpapp.stockportfolio.ui.common.theme.Color_FFFFFF
val myStockCountryList = listOf("한국 주식", "미국 주식")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val navItemList = remember { mutableStateListOf(NavItem.MyStock, NavItem.News) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var isShowBottomSheet by remember { mutableStateOf(false) }
    var titleText by remember { mutableStateOf("한국 주식") }
    Box {
        Scaffold(
            topBar = {
                Column {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(text = titleText)
                        },
                        actions = {
                            if (currentRoute == NavItem.MyStock.screenRoute) {
                                IconButton(
                                    onClick = { isShowBottomSheet = !isShowBottomSheet }
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.arrow_down),
                                        contentDescription = null
                                    )
                                }
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color_FFFFFF,
                            titleContentColor = Color_222222
                        )
                    )
                    Divider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = Color_F1F1F1
                    )
                }
            },
            bottomBar = {
                BottomNavigation(
                    modifier = Modifier.height(60.dp),
                    backgroundColor = Color.White,
                    contentColor = Color_222222,
                    elevation = 12.dp
                ) {
                    navItemList.forEach { item ->
                        val selectedColor = if (currentRoute == item.screenRoute) {
                            Color_222222
                        } else {
                            Color_888888
                        }
                        if (currentRoute != NavItem.MyStock.screenRoute) {
                            titleText = stringResource(id = item.title)
                        }
                        BottomNavigationItem(
                            modifier = Modifier.semantics {
                                contentDescription = context.getString(item.title)
                            },
                            selected = currentRoute == item.screenRoute,
                            onClick = {
                                if (item == NavItem.News) viewModel.getNewsList()
                                navController.navigation(item)
                            },
                            icon = {
                                Icon(
                                    modifier = Modifier.size(30.dp),
                                    painter = painterResource(id = item.icon),
                                    tint = selectedColor,
                                    contentDescription = null
                                )
                            },
                            label = {
                                Text(
                                    text = stringResource(id = item.title),
                                    color = selectedColor
                                )
                            },
                            selectedContentColor = Color_222222,
                            unselectedContentColor = Color_888888
                        )
                            stringResource(id = item.title)
                        }
                }
            }
        ) { paddingValues ->
            NavigationGraph(
                modifier = Modifier
                    .padding(paddingValues)
                    .background(color = Color.White),
                viewModel = viewModel,
                navController = navController
            )
        }
        val mainUiState by viewModel.mainUiState.collectAsStateWithLifecycle()
        if (mainUiState.toastMessageId != 0) {
            val message = if (mainUiState.toastErrorMessage != null) {
                context.getString(mainUiState.toastMessageId, mainUiState.toastErrorMessage)
            } else {
                context.getString(mainUiState.toastMessageId)
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.toastMessageShown()
        }
        if (mainUiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x66666666)),
                contentAlignment = Alignment.Center
            ) {
                LoadingWidget()
            }
        }
        if (isShowBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { isShowBottomSheet = false }
            ) {
                LazyColumn(
                    modifier = Modifier.padding(30.dp).fillMaxWidth()
                ) {
                    items(items = myStockCountryList) {
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.getAllMyStock(type = myStockCountryList.indexOf(it))
                                    isShowBottomSheet = false
                                    titleText = it
                                },
                            text = it,
                            fontSize = 18.sp,
                            color = Color_222222
                        )
                        Spacer(modifier = Modifier.size(20.dp))
                    }
                    item { Spacer(modifier = Modifier.size(20.dp)) }
                }
            }
        }
    }
}

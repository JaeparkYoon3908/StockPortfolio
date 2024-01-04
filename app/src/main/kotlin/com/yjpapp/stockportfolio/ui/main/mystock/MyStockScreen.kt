@file:OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)

package com.yjpapp.stockportfolio.ui.main.mystock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.yjpapp.data.model.MyStockData
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_4876C7
import com.yjpapp.stockportfolio.ui.common.theme.Color_666666
import com.yjpapp.stockportfolio.ui.common.theme.Color_CD4632
import com.yjpapp.stockportfolio.ui.main.MainViewModel
import com.yjpapp.stockportfolio.ui.main.mystock.dialog.MyStockPurchaseInputDialogContent
import com.yjpapp.stockportfolio.util.StockUtils

/**
 * MyStock UI Compose 분리
 * @author Yoon Jae-park
 * @since 2023.02
 */

@Composable
@ExperimentalMaterialApi
fun MyStockScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val myStockUiState by viewModel.myStockUiState.collectAsStateWithLifecycle()
    var showMyStockPurchaseInputDialog by remember { mutableStateOf(false) }
    if (showMyStockPurchaseInputDialog) {
        MyStockPurchaseInputDialogContent { dialogData, isComplete ->
            showMyStockPurchaseInputDialog = false
            if (isComplete) {
                viewModel.addMyStock(
                    MyStockData(
                        subjectName = dialogData.stockPriceInfo.itmsNm,
                        subjectCode = dialogData.stockPriceInfo.isinCd,
                        purchaseDate = dialogData.purchaseDate,
                        purchasePrice = dialogData.purchasePrice,
                        purchaseCount = dialogData.purchaseCount.toIntOrNull() ?: 0,
                        currentPrice = StockUtils.getNumInsertComma(dialogData.stockPriceInfo.clpr),
                        type = myStockUiState.type,
                        dayToDayPrice = dialogData.stockPriceInfo.vs,
                        dayToDayPercent = dialogData.stockPriceInfo.fltRt,
                        basDt = dialogData.stockPriceInfo.basDt,
                    )
                )
            }
        }
    }
    Column(modifier = modifier) {
        LazyColumn {
            item {
                TotalPriceComposable(
                    myStockViewModel = viewModel
                )
            }
            item { Spacer(modifier = Modifier.size(10.dp)) }
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "보유 주식",
                            fontWeight = FontWeight.Bold,
                            color = Color_222222,
                            fontSize = 17.sp,
                        )
                        Spacer(modifier = Modifier.size(10.dp))
                        if (myStockUiState.koreaStockInfoList.isNotEmpty()) {
                            Text(
                                text = "기준 일자 : ${myStockUiState.koreaStockInfoList.first().basDt}",
                                fontWeight = FontWeight.Light,
                                color = Color_666666,
                                fontSize = 13.sp,
                            )
                        }
                    }
                    Row {
                        IconButton(
                            modifier = Modifier.size(18.dp),
                            enabled = myStockUiState.koreaStockInfoList.isNotEmpty(),
                            onClick = { viewModel.refreshStockCurrentPriceInfo(1) } //TODO 프리퍼런스 정리
                        ) {
                            Icon(
                                modifier = Modifier.size(25.dp),
                                painter = painterResource(id = R.drawable.ic_refresh),
                                contentDescription = "새로고침",
                            )
                        }
                        Spacer(modifier = Modifier.size(20.dp))
                        IconButton(
                            modifier = Modifier.size(18.dp),
                            onClick = { showMyStockPurchaseInputDialog = true }
                        ) {
                            Icon(
                                modifier = Modifier.size(18.dp),
                                painter = painterResource(id = R.drawable.icon_add),
                                contentDescription = "추가하기",
                                tint = Color_222222
                            )
                        }
                    }
                }
            }
            item { Spacer(modifier = Modifier.size(20.dp)) }
            items(
                items = myStockUiState.koreaStockInfoList,
            ) { stockEntity ->
                MyStockListItemWidget(
                    viewModel = viewModel,
                    myStockData = stockEntity,
                )
            }
        }
    }
}

@Composable
private fun TotalPriceComposable(
    myStockViewModel: MainViewModel,
) {
    val myStockUiState by myStockViewModel.myStockUiState.collectAsStateWithLifecycle()
    Card(
        modifier = Modifier.padding(20.dp),
        shape = RoundedCornerShape(10.dp),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.common_15dp),
                    end = dimensionResource(id = R.dimen.common_15dp)
                )
        ) {
            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_20dp))
            ) {
                Text(
                    text = stringResource(R.string.MyStockFragment_Total_Purchase_Price),
                    color = Color_222222,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = StockUtils.getPriceNum(myStockUiState.totalPurchasePrice),
                    color = Color_222222,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = stringResource(R.string.MyStockFragment_Total_Evaluation_Amount),
                    color = Color_222222,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = StockUtils.getPriceNum(myStockUiState.totalEvaluationAmount),
                    color = Color_222222,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.common_10dp))
            ) {
                Text(
                    text = stringResource(R.string.Common_gains_losses),
                    color = Color_222222,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = StockUtils.getPriceNum(myStockUiState.totalGainPrice),
                    color = when {
                        StockUtils.getNumDeletedComma(myStockUiState.totalGainPrice)
                            .toDouble() > 0 -> Color_CD4632

                        StockUtils.getNumDeletedComma(myStockUiState.totalGainPrice)
                            .toDouble() < 0 -> Color_4876C7

                        else -> Color_CD4632
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }

            Row(
                modifier = Modifier
                    .padding(
                        top = dimensionResource(id = R.dimen.common_10dp),
                        bottom = dimensionResource(id = R.dimen.common_20dp)
                    )
            ) {
                Text(
                    text = stringResource(R.string.Common_GainPercent),
                    color = Color_222222,
                    fontSize = 16.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.30f)
                )
                Text(
                    text = myStockUiState.totalGainPricePercent,
                    color = when {
                        StockUtils.getNumDeletedComma(myStockUiState.totalGainPrice)
                            .toDouble() > 0 -> Color_CD4632

                        StockUtils.getNumDeletedComma(myStockUiState.totalGainPrice)
                            .toDouble() < 0 -> Color_4876C7

                        else -> Color_CD4632
                    },
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    modifier = Modifier
                        .weight(0.70f)
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    MyStockScreen()
}
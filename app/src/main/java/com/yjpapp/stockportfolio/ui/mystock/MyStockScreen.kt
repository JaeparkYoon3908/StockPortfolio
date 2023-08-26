package com.yjpapp.stockportfolio.ui.mystock

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yjpapp.stockportfolio.R
import com.yjpapp.stockportfolio.data.localdb.room.mystock.MyStockEntity
import com.yjpapp.stockportfolio.ui.common.theme.Color_222222
import com.yjpapp.stockportfolio.ui.common.theme.Color_4876C7
import com.yjpapp.stockportfolio.ui.common.theme.Color_CD4632
import com.yjpapp.stockportfolio.ui.mystock.dialog.MyStockPurchaseInputDialogContent
import com.yjpapp.stockportfolio.util.StockUtils
import kotlinx.coroutines.launch

/**
 * MyStock UI Compose 분리
 * @author Yoon Jae-park
 * @since 2023.02
 */
@Composable
@ExperimentalMaterialApi
fun MyStockScreen(
    modifier: Modifier = Modifier,
    viewModel: MyStockViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    var showMyStockPurchaseInputDialog by remember { mutableStateOf(false) }
    if (showMyStockPurchaseInputDialog) {
        MyStockPurchaseInputDialogContent { dialogData, isComplete ->
            showMyStockPurchaseInputDialog = false
            if (isComplete) {
                coroutineScope.launch {
                    val currentPriceData = viewModel.getCurrentPrice(subjectCode = dialogData.subjectName.code)
                    viewModel.addMyStock(
                        MyStockEntity(
                            subjectName = dialogData.subjectName.text,
                            subjectCode = dialogData.subjectName.code,
                            purchaseDate = dialogData.purchaseDate,
                            purchasePrice = dialogData.purchasePrice,
                            purchaseCount = dialogData.purchaseCount.toIntOrNull()?: 0,
                            currentPrice = currentPriceData.currentPrice,
                            gainPrice = (StockUtils.getNumDeletedComma(dialogData.purchasePrice).toInt() - StockUtils.getNumDeletedComma(currentPriceData.currentPrice).toInt()).toString(),
                            dayToDayPrice = currentPriceData.dayToDayPrice,
                            dayToDayPercent = currentPriceData.dayToDayPercent,
                            yesterdayPrice = currentPriceData.yesterdayPrice
                        )
                    )
                }
            }
        }
    }
    LazyColumn(modifier = modifier) {
        item {
            TotalPriceComposable(
                modifier = modifier,
                myStockViewModel = viewModel
            )
        }
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "보유 주식",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                )
                Row {
                    Image(
                        modifier = Modifier.size(20.dp).clickable { viewModel.refreshAllPrices() },
                        painter = painterResource(id = R.drawable.ic_refresh),
                        contentDescription = "새로고침",
                    )
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
            count = viewModel.myStockInfoList.size
        ) {
            MyStockListItemWidget(
                myStockViewModel = viewModel,
                myStockEntity = viewModel.myStockInfoList[it],
            )
        }
    }
}

@Composable
private fun TotalPriceComposable(
    modifier: Modifier = Modifier,
    myStockViewModel: MyStockViewModel,
) {
    val totalPurchasePrice by myStockViewModel.totalPurchasePrice.collectAsState()
    val totalEvaluationAmount by myStockViewModel.totalEvaluationAmount.collectAsState()
    val totalGainPrice by myStockViewModel.totalGainPrice.collectAsState()
    val totalGainPricePercent by myStockViewModel.totalGainPricePercent.collectAsState()
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
                    text = StockUtils.getPriceNum(totalPurchasePrice),
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
                    text = StockUtils.getPriceNum(totalEvaluationAmount),
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
                    text = StockUtils.getPriceNum(totalGainPrice),
                    color = when {
                        StockUtils.getNumDeletedComma(totalGainPrice).toDouble() > 0 -> Color_CD4632
                        StockUtils.getNumDeletedComma(totalGainPrice).toDouble() < 0 -> Color_4876C7
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
                    text = totalGainPricePercent,
                    color = when {
                        StockUtils.getNumDeletedComma(totalGainPrice).toDouble() > 0 -> Color_CD4632
                        StockUtils.getNumDeletedComma(totalGainPrice).toDouble() < 0 -> Color_4876C7
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